package com.tkdoon.ticket_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String result = "ERROR";
    private String message;
    private List<Map<String, String>> fieldErrors;
}
