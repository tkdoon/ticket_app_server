package com.tkdoon.ticket_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendFriendRequestDto {
    @NotNull(message = "相手のユーザーIDを指定してください")
    @Min(value = 1, message = "ユーザーIDは1以上を指定してください")
    private Integer addresseeId;
}
