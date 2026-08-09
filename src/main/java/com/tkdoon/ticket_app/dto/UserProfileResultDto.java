package com.tkdoon.ticket_app.dto;

import lombok.Data;

@Data
public class UserProfileResultDto {
    private String result = "SUCCESS";
    private int id;
    private String userName;
}
