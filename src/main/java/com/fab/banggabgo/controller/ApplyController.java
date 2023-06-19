package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.apply.ApproveUserForm;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.impl.ApplyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applicant")
public class ApplyController {

  private final ApplyServiceImpl applyService;

  @PatchMapping("/approve")
  public ResponseEntity<?> patchApprove(@AuthenticationPrincipal User user,
      @RequestBody ApproveUserForm form) {
    var result = applyService.patchApprove(user, ApproveUserForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PatchMapping("/refuse")
  public ResponseEntity<?> patchRefuse(@AuthenticationPrincipal User user,
      @RequestParam Integer applyId) {
    var result = applyService.patchRefuse(user, applyId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

}
