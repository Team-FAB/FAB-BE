package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  User user;

  @Mock
  UserRepository userRepository;
  @InjectMocks
  UserDetailsServiceImpl userDetailsService;

  @BeforeEach
  void init() {
    user = User.builder()
        .id(1)
        .email("test@test.com")
        .build();
  }

  @Test
  @DisplayName("이메일이 존재할경우")
  void loadByUsername() {
    // given
    String email = "test@test.com";
    // when
    when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));
    var result = userDetailsService.loadUserByUsername(email);
    Assertions.assertEquals(result, user);
    // then
    // Add your assertions or verifications here
  }

  @Test
  @DisplayName("존재하지 않을경우")
  void null_username() {
    //given
    var email = "wrongEmail@test.com";
    when(userRepository.findByEmail(email)).thenThrow(new RuntimeException());

    //when
    assertThrows(RuntimeException.class,() -> userRepository.findByEmail(email));
    //then
  }
}