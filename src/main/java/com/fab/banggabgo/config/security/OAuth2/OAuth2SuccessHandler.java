package com.fab.banggabgo.config.security.OAuth2;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.TokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        TokenDto tokenDto = TokenDto.builder()
            .rtk(tokenProvider.createRefreshToken())
            .atk(tokenProvider.createAccessToken(authentication.getName(),
                authentication.getAuthorities().stream().map(Object::toString)
                    .collect(Collectors.toList())))
            .build();

        response.getWriter().write(objectMapper.writeValueAsString(tokenDto));
    }
}
