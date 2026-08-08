package com.tkdoon.ticket_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserInfo(
        String email,
        Boolean email_verified,
        String name,
        String picture
) {
}
