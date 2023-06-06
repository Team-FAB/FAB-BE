package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.EmailCheckResultDto;
import com.fab.banggabgo.dto.LogOutResultDto;
import com.fab.banggabgo.dto.NameCheckResultDto;
import com.fab.banggabgo.dto.SignInRequestDto;
import com.fab.banggabgo.dto.SignInResultDto;
import com.fab.banggabgo.dto.SignUpRequestDto;
import javax.servlet.http.HttpServletRequest;

public interface SignService {

  void signUp(SignUpRequestDto dto);

  SignInResultDto signIn(SignInRequestDto dto);


  LogOutResultDto logout(HttpServletRequest req);

  EmailCheckResultDto emailCheck(String email);


  NameCheckResultDto nickNameCheck(String name);
}
