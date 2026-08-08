package com.tkdoon.ticket_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleTokenResponse(
        String access_token,
        String id_token,
        String token_type,
        Integer expires_in,
        String scope,
        String refresh_token
) {
}
