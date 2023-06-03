package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.SignInRequestDto;
import com.fab.banggabgo.dto.SignInResultDto;
import com.fab.banggabgo.dto.SignUpRequestDto;
import com.fab.banggabgo.dto.TokenDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  @Override
  public void signUp(SignUpRequestDto dto) {
    //todo 회원가입 메서드 구현
    if (userRepository.countByEmail(dto.getEmail()) > 0) {
      throw new RuntimeException("이미존재하는 이메일 입니다.");
    }
    User savedUser = userRepository.save(
        User.builder()
            .email(dto.getEmail())
            .name(dto.getName())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build()
    );
  }

  @Override
  public SignInResultDto signIn(SignInRequestDto dto) {
    //todo 로그인 메서드구현

    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new RuntimeException("존재하지않는 이메일"));
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
      throw new RuntimeException("비밀번호 불일치");
    }
    var atk = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
    var rtk = jwtTokenProvider.createRefreshToken();

    var result = SignInResultDto.builder()
        .token(TokenDto.builder()
            .atk(atk)
            .rtk(rtk)
            .build())
        .build();

    return result;
  }
}
