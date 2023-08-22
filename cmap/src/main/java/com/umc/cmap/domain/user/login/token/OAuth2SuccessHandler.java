package com.umc.cmap.domain.user.login.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.cmap.domain.user.login.dto.UserRequest;
import com.umc.cmap.domain.user.login.mapper.UserRequestMapper;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication)
    throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserRequest userRequest = userRequestMapper.toRequest(oAuth2User);

        Token token = tokenService.generateToken(userRequest.getEmail(), "USER");

        String url = makeRedirectUrl(token);
        httpServletResponse.sendRedirect(url);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Authorization",token.getToken());
        response.addHeader("RefreshToken",token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        response.sendRedirect("http://localhost/oauth2/redirect");
    }

    private String makeRedirectUrl(Token token){
        return UriComponentsBuilder.fromUriString("http://localhost/oauth2/redirect")
                .queryParam("Authorization", token.getToken())
                .queryParam("RefreshToken", token.getRefreshToken())
                .build().toUriString();
    }
}
