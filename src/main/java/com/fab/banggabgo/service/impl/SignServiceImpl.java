package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.sign.EmailCheckResultDto;
import com.fab.banggabgo.dto.sign.LogOutResultDto;
import com.fab.banggabgo.dto.sign.NickNameCheckResultDto;
import com.fab.banggabgo.dto.OAuth2ProfileDto;
import com.fab.banggabgo.dto.OAuth2SignInRequestDto;
import com.fab.banggabgo.dto.sign.SignInRequestDto;
import com.fab.banggabgo.dto.sign.SignInResultDto;
import com.fab.banggabgo.dto.sign.SignUpRequestDto;
import com.fab.banggabgo.dto.sign.TokenDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.SignService;
import com.fab.banggabgo.type.OAuth2RegistrationId;
import com.fab.banggabgo.type.UserRole;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignServiceImpl implements SignService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;

  private final RestTemplate restTemplate;

  @Value("${jwt.rtk-prefix}")
  String REDIS_PREFIX;

  @Override
  public void signUp(SignUpRequestDto dto) {
    checkDuplicate(dto.getEmail(), dto.getNickname());
    //todo 회원가입 메서드 구현
    User savedUser = userRepository.save(
        User.builder()
            .email(dto.getEmail())
            .nickname(dto.getNickname())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build()
    );
  }

  private void checkDuplicate(String value, ErrorCode errorCode) {
    if (userRepository.existsByEmailOrNickname(value, value)) {
      throw new CustomException(errorCode);
    }
  }

  private void checkDuplicate(String email, String nickname) {
    if (userRepository.existsByEmailOrNickname(email, nickname)) {
      throw new CustomException(ErrorCode.VALUES_ALREADY_EXISTS);
    }
  }

  @Override
  public SignInResultDto signIn(SignInRequestDto dto) {
    //todo 로그인 메서드구현

    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_EXISTS));
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
    }
    var atk = getAccessToken(user);
    var rtk = getRefreshToken();

    var result = SignInResultDto.builder()
        .token(TokenDto.builder()
            .atk(atk)
            .rtk(rtk)
            .build())
        .build();

    redisTemplate.opsForValue()
        .set(REDIS_PREFIX + user.getUsername(), rtk, jwtTokenProvider.getExpiration(rtk),
            TimeUnit.MILLISECONDS);

    return result;
  }

  @Override
  public SignInResultDto oauth2SignIn(OAuth2SignInRequestDto dto,
      OAuth2RegistrationId oAuth2RegistrationId) {
    OAuth2ProfileDto profile;
    //todo 로그인 메서드구현
    try {
      profile = getProfile(dto.getAccessToken(), oAuth2RegistrationId);
    } catch (ParseException | HttpClientErrorException e) {
      throw new CustomException(ErrorCode.FAIL_INFO_LOADING);
    }
    if (profile == null || profile.getEmail() == null) {
      throw new CustomException(ErrorCode.FAIL_INFO_LOADING);
    }

    User user = userRepository.findByEmail(profile.getEmail())
        .orElseGet(() -> userRepository.save(User.builder()
            .email(profile.getEmail())
            .nickname(profile.getNickName())
            .image(profile.getImageUrl())
            .roles(List.of(UserRole.USER_ROLE))
            .build()));

    var atk = getAccessToken(user);
    var rtk = getRefreshToken();

    var result = SignInResultDto.builder()
        .token(TokenDto.builder()
            .atk(atk)
            .rtk(rtk)
            .build())
        .build();

    redisTemplate.opsForValue()
        .set(REDIS_PREFIX + user.getUsername(), rtk, jwtTokenProvider.getExpiration(atk),
            TimeUnit.MILLISECONDS);

    return result;
  }



  @Override
  public LogOutResultDto logout(HttpServletRequest req) {
    var atk = jwtTokenProvider.resolveAtk(req);
    if (!jwtTokenProvider.validateToken(atk)) {
      throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

    var user = jwtTokenProvider.getAuthentication(atk);

    if (redisTemplate.opsForValue().get(REDIS_PREFIX + user.getName()) != null) {
      redisTemplate.delete(REDIS_PREFIX + user.getName());
    }

    var expiration = jwtTokenProvider.getExpiration(atk);
    redisTemplate.opsForValue().set(atk, "logout", expiration, TimeUnit.MILLISECONDS);

    return LogOutResultDto.builder()
        .expiredToken(TokenDto.builder()
            .atk(atk)
            .build())
        .build();
  }

  @Override
  public EmailCheckResultDto emailCheck(String email) {
    checkDuplicate(email, ErrorCode.EMAIL_ALREADY_EXISTS);
    return EmailCheckResultDto.builder()
        .msg("사용 가능한 이메일 입니다.")
        .build();
  }

  @Override
  public NickNameCheckResultDto nickNameCheck(String nickname) {
    checkDuplicate(nickname, ErrorCode.NICKNAME_ALREADY_EXISTS);
    return NickNameCheckResultDto.builder()
        .msg("사용 가능한 별명 입니다.")
        .build();
  }

  private OAuth2ProfileDto getProfile(String accessToken, OAuth2RegistrationId oAuth2RegistrationId)
      throws ParseException {
    String response;
    if (oAuth2RegistrationId.equals(OAuth2RegistrationId.KAKAO)) {
      response = getKakaoProfile(accessToken);
    } else {
      response = getGoogleProfile(accessToken);
    }
    return OAuth2ProfileDto.of(oAuth2RegistrationId,
        new JSONParser(response).parseObject());
  }

  private String getGoogleProfile(String accessToken) {
    return restTemplate.getForObject(
        "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken, String.class
    );
  }

  private String getKakaoProfile(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.add("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("property_keys", "[\"kakao_account.email\",\"kakao_account.profile\"]");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

    return restTemplate.postForObject(
        "https://kapi.kakao.com/v2/user/me",
        requestEntity, String.class
    );
  }

  private String getRefreshToken() {
    return jwtTokenProvider.createRefreshToken();
  }

  private String getAccessToken(User user) {
    return jwtTokenProvider.createAccessToken(user.getUsername(), user.getRoles());
  }

}
