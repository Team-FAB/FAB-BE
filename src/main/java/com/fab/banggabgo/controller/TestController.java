package com.fab.banggabgo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/test")
public class TestController {

  @GetMapping
  public ResponseEntity<?> getTest() {
    return ResponseEntity.ok().body("ok");
  }
}
