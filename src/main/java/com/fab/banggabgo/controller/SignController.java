package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.SignInRequestForm;
import com.fab.banggabgo.dto.SignUpRequestForm;
import com.fab.banggabgo.service.SignService;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SignController {

  private final SignService signService;

  @PostMapping("/register")
  public ResponseEntity<?> signUp(@RequestBody SignUpRequestForm form){
    signService.signUp(SignUpRequestForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }
  @PermitAll
  @GetMapping("/register/email-check")
  public ResponseEntity<?> emailCheck(@RequestParam String email){
    var result= signService.emailCheck(email);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();

  }
  @PermitAll
  @GetMapping("/register/nickname-check")
  public ResponseEntity<?> nickNameCheck(@RequestParam String nickname){
    var result= signService.nickNameCheck(nickname);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PostMapping("/login")
  public ResponseEntity<?> signIn(@RequestBody SignInRequestForm form){
    var result = signService.signIn(SignInRequestForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest req){
    var result = signService.logout(req);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
