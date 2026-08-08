package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.GoogleUserInfo;
import com.tkdoon.ticket_app.dto.LogoutResultDto;
import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.service.AuthService;
import com.tkdoon.ticket_app.service.GoogleOAuthClient;
import com.tkdoon.ticket_app.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private static final String STATE_COOKIE = "oauth_state";
    private static final String VERIFIER_COOKIE = "oauth_verifier";
    private static final Duration OAUTH_COOKIE_TTL = Duration.ofMinutes(10);
    private static final Duration TOKEN_COOKIE_TTL = Duration.ofHours(1);

    private final AuthService authService;
    private final JwtService jwtService;
    private final GoogleOAuthClient googleOAuthClient;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public AuthController(AuthService authService, JwtService jwtService, GoogleOAuthClient googleOAuthClient) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.googleOAuthClient = googleOAuthClient;
    }

    @GetMapping("/login/google")
    public ResponseEntity<Void> startGoogleLogin() {
        String state = UUID.randomUUID().toString();
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = sha256Base64Url(codeVerifier);

        String authUrl = googleOAuthClient.buildAuthorizationUrl(state, codeChallenge);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, oauthCookie(STATE_COOKIE, state, OAUTH_COOKIE_TTL).toString());
        headers.add(HttpHeaders.SET_COOKIE, oauthCookie(VERIFIER_COOKIE, codeVerifier, OAUTH_COOKIE_TTL).toString());
        headers.setLocation(URI.create(authUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback/google")
    public ResponseEntity<Void> googleCallback(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request) {

        if (error != null) {
            log.warn("Google auth callback returned error: {}", error);
            return errorRedirect();
        }
        if (code == null || state == null) {
            log.warn("Google auth callback missing required params (code/state)");
            return errorRedirect();
        }

        String expectedState = readCookie(request, STATE_COOKIE);
        String codeVerifier = readCookie(request, VERIFIER_COOKIE);
        if (expectedState == null || codeVerifier == null) {
            log.warn("OAuth state/verifier cookie missing on callback");
            return errorRedirect();
        }
        if (!constantTimeEquals(expectedState, state)) {
            log.warn("OAuth state mismatch (possible CSRF attempt)");
            return errorRedirect();
        }

        try {
            GoogleUserInfo userInfo = googleOAuthClient.exchangeCodeAndFetchUser(code, codeVerifier);
            UserEntity user = authService.login(userInfo.email(), userInfo.name(), userInfo.picture());
            String jwt = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, tokenCookie(jwt, TOKEN_COOKIE_TTL).toString());
            headers.add(HttpHeaders.SET_COOKIE, clearOauthCookie(STATE_COOKIE).toString());
            headers.add(HttpHeaders.SET_COOKIE, clearOauthCookie(VERIFIER_COOKIE).toString());
            headers.setLocation(URI.create(frontendUrl + "/?login=ok"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (Exception e) {
            log.warn("Google auth callback failed during token exchange", e);
            return errorRedirect();
        }
    }

    @PostMapping("/logout")
    public LogoutResultDto logout(HttpServletResponse response) {
        ResponseCookie cookie = tokenCookie("", Duration.ZERO);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new LogoutResultDto();
    }

    private ResponseEntity<Void> errorRedirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, clearOauthCookie(STATE_COOKIE).toString());
        headers.add(HttpHeaders.SET_COOKIE, clearOauthCookie(VERIFIER_COOKIE).toString());
        headers.setLocation(URI.create(frontendUrl + "/?login=error"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    private ResponseCookie oauthCookie(String name, String value, Duration ttl) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(ttl)
                .build();
    }

    private ResponseCookie clearOauthCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(0)
                .build();
    }

    private ResponseCookie tokenCookie(String value, Duration ttl) {
        return ResponseCookie.from("token", value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(ttl)
                .build();
    }

    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    private String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Base64Url(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
