package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.SignInRequestDto;
import com.fab.banggabgo.dto.SignInResultDto;
import com.fab.banggabgo.dto.SignUpRequestDto;

public interface SignService {

  void signUp(SignUpRequestDto dto);

  SignInResultDto signIn(SignInRequestDto dto);
}
