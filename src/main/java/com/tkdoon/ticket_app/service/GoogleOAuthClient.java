package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.GoogleTokenResponse;
import com.tkdoon.ticket_app.dto.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleOAuthClient {

    private static final String AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestClient restClient;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    public GoogleOAuthClient(RestClient googleRestClient) {
        this.restClient = googleRestClient;
    }

    public String buildAuthorizationUrl(String state, String codeChallenge) {
        return UriComponentsBuilder.fromUriString(AUTH_ENDPOINT)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid email profile")
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .queryParam("access_type", "online")
                .queryParam("prompt", "select_account")
                .encode()
                .build()
                .toUriString();
    }

    public GoogleUserInfo exchangeCodeAndFetchUser(String code, String codeVerifier) {
        GoogleTokenResponse tokenRes = exchangeCodeForToken(code, codeVerifier);
        if (tokenRes == null || tokenRes.access_token() == null) {
            throw new IllegalStateException("Google token response missing access_token");
        }
        return fetchUserInfo(tokenRes.access_token());
    }

    private GoogleTokenResponse exchangeCodeForToken(String code, String codeVerifier) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");
        form.add("code_verifier", codeVerifier);

        return restClient.post()
                .uri(TOKEN_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(GoogleTokenResponse.class);
    }

    private GoogleUserInfo fetchUserInfo(String accessToken) {
        GoogleUserInfo info = restClient.get()
                .uri(USERINFO_ENDPOINT)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(GoogleUserInfo.class);
        if (info == null || info.email() == null) {
            throw new IllegalStateException("Google userinfo response missing email");
        }
        if (Boolean.FALSE.equals(info.email_verified())) {
            throw new IllegalStateException("Google account email is not verified");
        }
        return info;
    }
}
