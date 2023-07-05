package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.apply.ApplyDeleteResultDto;
import com.fab.banggabgo.dto.apply.ApplyListResultDto;
import com.fab.banggabgo.dto.apply.ApproveUserForm;
import com.fab.banggabgo.dto.apply.ApproveUserResultDto;
import com.fab.banggabgo.dto.apply.RefuseUserResultDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.impl.ApplyServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Apply Controller 지원 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applicant")
public class ApplyController {

  private final ApplyServiceImpl applyService;
  @ApiOperation(
      value = "룸메이트 승인",
      notes = "지원자를 승인합니다."
  )
  @PatchMapping("/approve")
  public ResponseEntity<ApiResponse<ApproveUserResultDto>> patchApprove(@AuthenticationPrincipal User user,
      @RequestBody ApproveUserForm form) {
    var result = applyService.patchApprove(user, ApproveUserForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "룸메이트 거절",
      notes = "지원자를 거절합니다."
  )
  @PatchMapping("/refuse/{applyId}")
  public ResponseEntity<ApiResponse<RefuseUserResultDto>> patchRefuse(@AuthenticationPrincipal User user,
      @PathVariable Integer applyId) {
    var result = applyService.patchRefuse(user, applyId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "룸메이트 지원 삭제",
      notes = "신청한 룸메이트 지원을 삭제합니다."
  )
  @DeleteMapping("/{applyId}")
  public ResponseEntity<ApiResponse<ApplyDeleteResultDto>> deleteApplicant(@AuthenticationPrincipal User user,
      @PathVariable Integer applyId) {
    var result = applyService.deleteApply(user, applyId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "알림 가져오기",
      notes = "로그인 유저의 알림을 가져옵니다."
  )
  @GetMapping("/notices")
  public ResponseEntity<ApiResponse<ApplyListResultDto>> getNotices(@AuthenticationPrincipal User user,
      @RequestParam Integer page, @RequestParam Integer size) {
    var result = applyService.getNotices(user, page, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "알림 삭제",
      notes = "id 에 해당하는 알림을 삭제합니다."
  )
  @DeleteMapping("/notice/{applyId}")
  public ResponseEntity<ApiResponse<ApplyDeleteResultDto>> deleteNotices(@AuthenticationPrincipal User user,
      @PathVariable Integer applyId) {
    var result = applyService.deleteNotice(user, applyId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
