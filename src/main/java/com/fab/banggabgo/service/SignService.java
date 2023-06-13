package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.sign.EmailCheckResultDto;
import com.fab.banggabgo.dto.sign.LogOutResultDto;
import com.fab.banggabgo.dto.sign.NickNameCheckResultDto;
import com.fab.banggabgo.dto.sign.SignInRequestDto;
import com.fab.banggabgo.dto.sign.SignInResultDto;
import com.fab.banggabgo.dto.sign.SignUpRequestDto;
import javax.servlet.http.HttpServletRequest;

public interface SignService {

  void signUp(SignUpRequestDto dto);

  SignInResultDto signIn(SignInRequestDto dto);


  LogOutResultDto logout(HttpServletRequest req);

  EmailCheckResultDto emailCheck(String email);


  NickNameCheckResultDto nickNameCheck(String name);
}
