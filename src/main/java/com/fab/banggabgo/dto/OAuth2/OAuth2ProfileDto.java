package com.fab.banggabgo.dto.OAuth2;


import com.fab.banggabgo.type.OAuth2RegistrationId;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OAuth2ProfileDto {

  String imageUrl;
  String nickName;
  String email;


  public static OAuth2ProfileDto of(OAuth2RegistrationId registrationId,
      Map<String, Object> Attributes) {
    if (registrationId.equals(OAuth2RegistrationId.KAKAO)) {
      return OAuth2ProfileDto.ofKakao(Attributes);
    } else {
      return OAuth2ProfileDto.ofGoogle(Attributes);
    }
  }

  public static OAuth2ProfileDto ofKakao(Map<String, Object> attributes) {
    Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(
        "kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
    Map<String, Object> profile = (Map<String, Object>) kakao_account.get(
        "profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.
    return new OAuth2ProfileDto(
        (String) profile.get("profile_image_url"),
        (String) profile.get("nickname"),
        (String) kakao_account.get("email"));
  }

  public static OAuth2ProfileDto ofGoogle(Map<String, Object> attributes) {
    return new OAuth2ProfileDto(
        (String) attributes.get("picture"),
        (String) attributes.get("name"),
        (String) attributes.get("email"));
  }
}
