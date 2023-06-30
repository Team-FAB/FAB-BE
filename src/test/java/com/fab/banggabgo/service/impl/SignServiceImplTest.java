package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.sign.SignInRequestDto;
import com.fab.banggabgo.dto.sign.SignInRequestForm;
import com.fab.banggabgo.dto.sign.SignInResultDto;
import com.fab.banggabgo.dto.sign.SignUpRequestDto;
import com.fab.banggabgo.dto.sign.TokenDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@ExtendWith(MockitoExtension.class)
class SignServiceImplTest {

  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  JwtTokenProvider jwtTokenProvider;

  @Mock
  RedisTemplate<String, String> redisTemplate;
  @Mock
  ValueOperations<String, String> valueOperations;
  @Mock
  HttpServletRequest request;

  @Mock
  UsernamePasswordAuthenticationToken user;
  @InjectMocks
  SignServiceImpl signService;

  @Mock
  RestTemplate restTemplate;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  WebClient webClient;

  @Nested
  @DisplayName("SignUp - 회원가입")
  class SignUp {

    @DisplayName("회원가입- 정상케이스")
    @Test
    void signUpSuccess() {
      //given
      var user = User.builder()
          .email("test@test.com")
          .nickname("테스터")
          .build();
      var requestDto = SignUpRequestDto.builder()
          .nickname("테스터")
          .email("test@test.com")
          .build();
      //when
      when(userRepository.existsByEmailOrNickname(user.getEmail(), user.getNickname())).thenReturn(
          false);
      when(userRepository.save(any(User.class))).thenReturn(user);
      //then
      signService.signUp(requestDto);
      verify(userRepository, times(1)).save(any());
    }

    @DisplayName("회원가입- 이미 이메일이나 닉네임이 존재할경우")
    @Test
    void signUp_emailExists() {
      //given
      var user = User.builder()
          .email("test@email.com")
          .nickname("테스터")
          .build();
      var requestDto = SignUpRequestDto.builder()
          .nickname("테스터")
          .email("test@email.com")
          .build();
      //when
      when(userRepository.existsByEmailOrNickname(user.getEmail(), user.getNickname())).thenReturn(
          true);

      //then
      assertThrows(RuntimeException.class, () -> signService.signUp(requestDto));
    }
  }

  @Nested
  @DisplayName(" SignIn - 로그인")
  class SignIn {

    String email = "test@email.com";
    String password = "testpass";

    SignInRequestForm form = SignInRequestForm.builder()
        .email(email)
        .password(password)
        .build();
    SignInRequestDto dto = SignInRequestForm.toDto(form);

    @DisplayName(" 로그인 - 정상케이스")
    @Test
    void signUpSuccess() {
      //given
      var user = User.builder()
          .email("test@email.com")
          .nickname("테스터")
          .build();
      var stub_result = SignInResultDto.builder()
          .token(
              TokenDto.builder()
                  .atk("accesstoken")
                  .rtk("rtktoken")
                  .build()
          )
          .build();
      //when
      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(any(), any())).thenReturn(true);
      when(passwordEncoder.matches(any(), any())).thenReturn(true);
      when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      var result = signService.signIn(dto);

      //then
      verify(userRepository, times(1)).findByEmail(email);
      verify(passwordEncoder, times(1)).matches(password, user.getPassword());
      verify(jwtTokenProvider, times(1)).createAccessToken(user.getEmail(), user.getRoles());
      verify(jwtTokenProvider, times(1)).createRefreshToken();
      verify(jwtTokenProvider, times(1)).getExpiration(anyString());
      assertEquals(result.getToken().getAtk(), stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(), stub_result.getToken().getRtk());
    }

    @DisplayName(" 로그인 - 비밀번호가 다른경우")
    @Test
    void signUp_emailExists() {
      //given
      var user = User.builder()
          .email("test@email.com")
          .nickname("테스터")
          .build();
      //when

      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(any(), any())).thenReturn(true);
      when(passwordEncoder.matches(any(), any())).thenReturn(false);
      //then
      assertThrows(RuntimeException.class, () -> signService.signIn(dto));
    }
  }

  @Nested
  @DisplayName(" Logout- 로그아웃")
  class Logout {

    String stub_atk = "stub_atk";

    @Test
    @DisplayName("Logout - 만료된토큰")
    void logout_expiredATk() { // 만료된 토큰일경우
      //given
      //when
      when(jwtTokenProvider.resolveAtk(any())).thenReturn(stub_atk);
      when(jwtTokenProvider.validateToken(stub_atk)).thenReturn(false);

      //then
      assertThrows(RuntimeException.class, () -> signService.logout(any()));
    }

    @Test
    @DisplayName("Logout - 정상 로그아웃")
    void logout() {
      //given
      long expiration = 3600000; // 1 hour

      //when
      when(jwtTokenProvider.resolveAtk(any(HttpServletRequest.class))).thenReturn(stub_atk);
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      when(jwtTokenProvider.validateToken(stub_atk)).thenReturn(true);
      when(redisTemplate.opsForValue().get(anyString())).thenReturn(stub_atk);
      when(jwtTokenProvider.getExpiration(stub_atk)).thenReturn(expiration);
      when(jwtTokenProvider.getAuthentication(stub_atk)).thenReturn(user);
      when(user.getName()).thenReturn("test");
      //then
      signService.logout(request);
      verify(redisTemplate.opsForValue(), times(1)).set(anyString(), eq("logout"), eq(expiration),
          eq(TimeUnit.MILLISECONDS));

    }

  }

}