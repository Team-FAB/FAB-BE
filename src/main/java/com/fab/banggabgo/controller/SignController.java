package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.dto.SignInRequestForm;
import com.fab.banggabgo.dto.SignUpRequestForm;
import com.fab.banggabgo.service.SignService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/sign")
public class SignController {

  private final SignService signService;

  @PostMapping("/sign-up")
  public ResponseEntity<?> signUp(@RequestBody SignUpRequestForm form){
    signService.signUp(SignUpRequestForm.toDto(form));
    return ResponseEntity.created(null).build();
  }
  @PostMapping("/sign-in")
  public ResponseEntity<?> signIn(@RequestBody SignInRequestForm form){
    var result = signService.signIn(SignInRequestForm.toDto(form));
    return ResponseEntity.ok().body(result);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest req){
    var result = signService.logout(req);
    return ResponseEntity.ok().body(result);
  }
}
