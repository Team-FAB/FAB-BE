package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.EmailCheckResultDto;
import com.fab.banggabgo.dto.LogOutResultDto;
import com.fab.banggabgo.dto.NameCheckResultDto;
import com.fab.banggabgo.dto.OAuth2SignInRequestDto;
import com.fab.banggabgo.dto.SignInRequestDto;
import com.fab.banggabgo.dto.SignInResultDto;
import com.fab.banggabgo.dto.SignUpRequestDto;
import com.fab.banggabgo.type.OAuth2RegistrationId;
import javax.servlet.http.HttpServletRequest;

public interface SignService {

  void signUp(SignUpRequestDto dto);

  SignInResultDto signIn(SignInRequestDto dto);


  SignInResultDto oauth2SignIn(OAuth2SignInRequestDto dto,
      OAuth2RegistrationId oAuth2RegistrationId);

  LogOutResultDto logout(HttpServletRequest req);

  EmailCheckResultDto emailCheck(String email);


  NameCheckResultDto nickNameCheck(String name);
}
