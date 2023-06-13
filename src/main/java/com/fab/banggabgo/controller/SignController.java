package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.OAuth2.OAuth2SignInRequestForm;
import com.fab.banggabgo.dto.sign.SignInRequestForm;
import com.fab.banggabgo.dto.sign.SignUpRequestForm;
import com.fab.banggabgo.service.SignService;
import com.fab.banggabgo.type.OAuth2RegistrationId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Sign Controller 로그인 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SignController {

  private final SignService signService;

  @ApiOperation(
      value = "사용자 회원가입",
      notes = "사용자에게 입력을 받고 회원가입 합니다."
  )
  @PostMapping("/register")
  public ResponseEntity<?> signUp(@RequestBody SignUpRequestForm form) {
    signService.signUp(SignUpRequestForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @ApiOperation(
      value = "가입 이메일 체크",
      notes = "사용자로부터 이메일을 입력 받고 이미 가입된 이메일 인지 체크"
  )
  @GetMapping("/register/email-check")
  public ResponseEntity<?> emailCheck(@RequestParam String email) {
    var result = signService.emailCheck(email);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();

  }

  @ApiOperation(
      value = "가입 닉네임 체크 ",
      notes = "사용자로부터 닉네임을 입력 받고 이미 가입된 닉네임 인지 체크"
  )
  @GetMapping("/register/nickname-check")
  public ResponseEntity<?> nickNameCheck(@RequestParam String nickname) {
    var result = signService.nickNameCheck(nickname);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "로그인",
      notes = "사용자로부터 아이디와 패스워드를 입력 받고 로그인에 대한 응답값 반환"
  )
  @PostMapping("/login")
  public ResponseEntity<?> signIn(@RequestBody SignInRequestForm form) {
    var result = signService.signIn(SignInRequestForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "kakao로그인",
      notes = "kakao에서 발급한 access_token을 받고 로그인에 대한 응답값 반환"
  )
  @PostMapping("/login/kakao")
  public ResponseEntity<?> signInKakao(@RequestBody OAuth2SignInRequestForm form) {
    var result = signService.oauth2SignIn(OAuth2SignInRequestForm.toDto(form),
        OAuth2RegistrationId.KAKAO);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "google로그인",
      notes = "google에서 발급한 access_token을 받고 로그인에 대한 응답값 반환"
  )
  @PostMapping("/login/google")
  public ResponseEntity<?> signInGoogle(@RequestBody OAuth2SignInRequestForm form) {
    var result = signService.oauth2SignIn(OAuth2SignInRequestForm.toDto(form),
        OAuth2RegistrationId.GOOGLE);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "로그아웃",
      notes = "로그인된 사용자를 로그아웃 처리함"
  )
  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest req) {
    var result = signService.logout(req);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
