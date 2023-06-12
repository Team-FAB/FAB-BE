package com.fab.banggabgo.dto;


import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OAuth2ProfileForm {
  String imageUrl;
  String nickName;
  String email;

  public static OAuth2ProfileForm of(String registrationId, Map<String, Object> Attributes){
    if(registrationId.equals("kakao")){
      return OAuth2ProfileForm.ofKakao(Attributes);
    }
    return null;
  }

  public static OAuth2ProfileForm ofKakao(Map<String, Object> attributes){
    Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
    Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.
    return new OAuth2ProfileForm (
        (String) profile.get("profile_image_url"),
        (String) profile.get("nickname"),
        (String) kakao_account.get("email"));
  }
}
