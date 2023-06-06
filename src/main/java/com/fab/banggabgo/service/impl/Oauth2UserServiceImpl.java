package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.dto.OAuthAttributes;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@RequiredArgsConstructor

public class Oauth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());
//        회원이 없다면 회원가입
        User user = userRepository.findByEmail(oAuthAttributes.getEmail()).orElse(
            new User()
        );
        user.setEmail(oAuthAttributes.getEmail());
        user.setNickname(oAuthAttributes.getName());
        user.setRoles(List.of("ROLE_USER"));
        userRepository.save(user);
        return new DefaultOAuth2User(user.getAuthorities(), oAuthAttributes.toEntity(), "email");
    }
}
