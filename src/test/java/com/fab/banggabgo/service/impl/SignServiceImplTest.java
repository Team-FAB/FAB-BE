package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.OAuth2.OAuth2SignInRequestDto;
import com.fab.banggabgo.dto.OAuth2.OAuth2SignInRequestForm;
import com.fab.banggabgo.dto.sign.SignInRequestDto;
import com.fab.banggabgo.dto.sign.SignInRequestForm;
import com.fab.banggabgo.dto.sign.SignInResultDto;
import com.fab.banggabgo.dto.sign.SignUpRequestDto;
import com.fab.banggabgo.dto.sign.TokenDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.type.OAuth2RegistrationId;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


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
  @DisplayName(" OAuth2SignIn - 소셜 로그인")
  class OAuth2SignIn {

    String kakaoJson = "{\n"
        + "    \"kakao_account\": {\n"
        + "        \"profile\": {\n"
        + "            \"nickname\": \"테스터\",\n"
        + "            \"profile_image_url\": \"image\"\n"
        + "        },\n"
        + "        \"email\": \"test@email.com\"\n"
        + "    }\n"
        + "}";
    String googleJson = "{ "
        + " \"email\": \"test@email.com\","
        + " \"name\": \"테스터\","
        + " \"picture\": \"profile_image_url\""
        + " }";
    OAuth2SignInRequestForm form = OAuth2SignInRequestForm.builder()
        .code("ThisIsAccessToken")
        .build();
    OAuth2SignInRequestDto dto = OAuth2SignInRequestForm.toDto(form);

    @DisplayName(" 카카오 로그인 - 회원 가입되어 있던케이스")
    @Test
    void signInWithoutSignUpSuccess() {
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
      when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(kakaoJson);
      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      var result = signService.oauth2SignIn(dto, OAuth2RegistrationId.KAKAO);

      //then
      verify(userRepository, times(1)).findByEmail(user.getEmail());
      verify(jwtTokenProvider, times(1)).createAccessToken(user.getEmail(), user.getRoles());
      verify(jwtTokenProvider, times(1)).createRefreshToken();
      verify(jwtTokenProvider, times(1)).getExpiration(anyString());
      assertEquals(result.getToken().getAtk(), stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(), stub_result.getToken().getRtk());
    }

    @DisplayName(" 카카오 로그인 - 회원 가입이 필요한 케이스")
    @Test
    void signInWithSignUpSuccess() {
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
      when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(kakaoJson);
      when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
      when(userRepository.save(any())).thenReturn(user);
      when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      var result = signService.oauth2SignIn(dto, OAuth2RegistrationId.KAKAO);

      //then
      verify(userRepository, times(1)).findByEmail(user.getEmail());
      verify(userRepository, times(1)).save(any());
      verify(jwtTokenProvider, times(1)).createAccessToken(user.getEmail(), user.getRoles());
      verify(jwtTokenProvider, times(1)).createRefreshToken();
      verify(jwtTokenProvider, times(1)).getExpiration(anyString());
      assertEquals(result.getToken().getAtk(), stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(), stub_result.getToken().getRtk());
    }

    @DisplayName(" 구글 로그인 - 회원 가입되어 있던케이스")
    @Test
    void signInWithoutSignUpGoogleSuccess() {
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
      when(restTemplate.getForObject(anyString(), any())).thenReturn(googleJson);
      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      var result = signService.oauth2SignIn(dto, OAuth2RegistrationId.GOOGLE);

      //then
      verify(userRepository, times(1)).findByEmail(user.getEmail());
      verify(jwtTokenProvider, times(1)).createAccessToken(user.getEmail(), user.getRoles());
      verify(jwtTokenProvider, times(1)).createRefreshToken();
      verify(jwtTokenProvider, times(1)).getExpiration(anyString());
      assertEquals(result.getToken().getAtk(), stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(), stub_result.getToken().getRtk());
    }

    @DisplayName(" 구글 로그인 - 회원 가입이 필요한 케이스")
    @Test
    void signInWithSignUpGoogleSuccess() {
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
      when(restTemplate.getForObject(anyString(), any())).thenReturn(googleJson);
      when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
      when(userRepository.save(any())).thenReturn(user);
      when(jwtTokenProvider.createAccessToken(any(), any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");
      when(redisTemplate.opsForValue()).thenReturn(valueOperations);
      var result = signService.oauth2SignIn(dto, OAuth2RegistrationId.GOOGLE);

      //then
      verify(userRepository, times(1)).findByEmail(user.getEmail());
      verify(userRepository, times(1)).save(any());
      verify(jwtTokenProvider, times(1)).createAccessToken(user.getEmail(), user.getRoles());
      verify(jwtTokenProvider, times(1)).createRefreshToken();
      verify(jwtTokenProvider, times(1)).getExpiration(anyString());
      assertEquals(result.getToken().getAtk(), stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(), stub_result.getToken().getRtk());
    }

    @DisplayName(" 카카오 로그인 - accessToken이 올바르지 않은경우")
    @Test
    void signIn_wrongAccessToken() {

      when(restTemplate.postForObject(anyString(), any(), any())).thenThrow(
          HttpClientErrorException.class);

      assertThrows(CustomException.class, () -> signService.oauth2SignIn(
          OAuth2SignInRequestDto.builder()
              .code("asdkwekwlwekfwlekwl")
              .build(), OAuth2RegistrationId.KAKAO));
    }

    @DisplayName(" 구글 로그인 - 구글에서 받은 json의 email이 없어진 경우")
    @Test
    void signIn_wrongAcceptJson() {
      var changeJson = "{ "
          + " \"name\": \"테스터\","
          + " \"picture\": \"profile_image_url\""
          + " }";

      when(restTemplate.getForObject(anyString(), any())).thenReturn(changeJson);

      assertThrows(CustomException.class, () -> signService.oauth2SignIn(
          dto, OAuth2RegistrationId.GOOGLE));
    }

    @DisplayName(" 카카오 로그인 - 카카오에서 받은 json의 형식이 달라져 email을 찾기힘든 경우")
    @Test
    void signIn_wrongAcceptGoogleJson() {
      var changeJson = "{\n"
          + "    \"kakao_account\": {\n"
          + "        \"profile\": {\n"
          + "            \"nickname\": \"테스터\",\n"
          + "            \"profile_image_url\": \"image\",\n"
          + "            \"email\": \"test@email.com\"\n"
          + "        }\n"
          + "    }\n"
          + "}";

      when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(changeJson);

      assertThrows(CustomException.class, () -> signService.oauth2SignIn(
          dto, OAuth2RegistrationId.KAKAO));
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