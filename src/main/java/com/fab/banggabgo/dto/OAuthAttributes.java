package com.fab.banggabgo.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
        Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
        Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get(
            "kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get(
            "profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.
        if (kakao_account == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("403"), "이메일 권한이 필요합니다.");
        }
        profile = profile == null ? new HashMap<>() : profile;
        return new OAuthAttributes(attributes,
            userNameAttributeName,
            (String) profile.getOrDefault("nickname", null),
            (String) kakao_account.get("email"),
            (String) profile.getOrDefault("profile_image_url", null));
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
        Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(
            "response");    // 네이버에서 받은 데이터에서 프로필 정보다 담긴 response 값을 꺼낸다.

        return new OAuthAttributes(attributes,
            userNameAttributeName,
            (String) response.get("name"),
            (String) response.get("email"),
            (String) response.get("profile_image"));
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {

        return new OAuthAttributes(attributes,
            userNameAttributeName,
            (String) attributes.get("name"),
            (String) attributes.get("email"),
            (String) attributes.get("picture"));
    }

    public Map<String, Object> toEntity() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", this.name);
        attributes.put("email", this.email);
        attributes.put("picture", this.picture);
        return attributes;
    }
}
