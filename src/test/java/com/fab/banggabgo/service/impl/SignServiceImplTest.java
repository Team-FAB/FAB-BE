package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.SignInRequestDto;
import com.fab.banggabgo.dto.SignInRequestForm;
import com.fab.banggabgo.dto.SignInResultDto;
import com.fab.banggabgo.dto.SignUpRequestDto;
import com.fab.banggabgo.dto.TokenDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.SignService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@ExtendWith(MockitoExtension.class)
class SignServiceImplTest {
  @Mock
  UserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  JwtTokenProvider jwtTokenProvider;

  @InjectMocks
  SignServiceImpl signService;
  @Nested
  @DisplayName("SignUp - 회원가입")
  class SignUp{

    @DisplayName("회원가입- 정상케이스")
    @Test
    void signUpSuccess(){
        //given
      var user =User.builder()
          .email("test@test.com")
          .name("테스터")
          .build();
      var requestDto=SignUpRequestDto.builder()
          .name("테스터")
          .email("test@test.com")
          .build();
        //when
      when(userRepository.countByEmail(user.getEmail())).thenReturn(0);
      when(userRepository.save(any(User.class))).thenReturn(user);


      signService.signUp(requestDto);
        //then
      verify(userRepository,times(1)).save(any());
    }
    @DisplayName("회원가입- 이미 이메일이 존재할경우")
    @Test
    void signUp_emailExists(){
      //given
      var user =User.builder()
          .email("test@email.com")
          .name("테스터")
          .build();
      var requestDto=SignUpRequestDto.builder()
          .name("테스터")
          .email("test@email.com")
          .build();
      //when
      when(userRepository.countByEmail(user.getEmail())).thenReturn(1);

      //then
      assertThrows(RuntimeException.class,() -> signService.signUp(requestDto));
    }
  }
  @Nested
  @DisplayName(" SignIn - 로그인")
  class SignIn{
    String email="test@email.com";
    String password="testpass";

    SignInRequestForm form = SignInRequestForm.builder()
        .email(email)
        .password(password)
        .build();
    SignInRequestDto dto=SignInRequestForm.toDto(form);
    @DisplayName(" 로그인 - 정상케이스")
    @Test
    void signUpSuccess(){
      //given
      var user =User.builder()
          .email("test@email.com")
          .name("테스터")
          .build();
      var stub_result=SignInResultDto.builder()
          .token(
              TokenDto.builder()
                  .atk("accesstoken")
                  .rtk("rtktoken")
                  .build()
          )
          .build();
      //when
      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(any(), any())).thenReturn(true);when(passwordEncoder.matches(any(), any())).thenReturn(true);
      when(jwtTokenProvider.createAccessToken(any(),any())).thenReturn("accesstoken");
      when(jwtTokenProvider.createRefreshToken()).thenReturn("rtktoken");


      //then
      var result= signService.signIn(dto);
      assertEquals(result.getToken().getAtk(),stub_result.getToken().getAtk());
      assertEquals(result.getToken().getRtk(),stub_result.getToken().getRtk());
    }
    @DisplayName(" 로그인 - 비밀번호가 다른경우")
    @Test
    void signUp_emailExists() {
      //given
      var user =User.builder()
          .email("test@email.com")
          .name("테스터")
          .build();
      //when

      when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
      when(passwordEncoder.matches(any(), any())).thenReturn(true);when(passwordEncoder.matches(any(), any())).thenReturn(false);
      //then
      assertThrows(RuntimeException.class,() -> signService.signIn(dto));
    }
  }

}