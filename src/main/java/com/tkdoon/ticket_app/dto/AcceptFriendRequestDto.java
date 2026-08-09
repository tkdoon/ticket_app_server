package com.tkdoon.ticket_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AcceptFriendRequestDto {
    @NotNull(message = "申請者のユーザーIDを指定してください")
    @Min(value = 1, message = "ユーザーIDは1以上を指定してください")
    private Integer requesterId;
}
