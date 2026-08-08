package com.tkdoon.ticket_app.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTicketRequestDto {

    @NotBlank(message = "タイトルは必須です")
    private String title;

    private String description;

    @NotNull(message = "有効期限は必須です")
    @FutureOrPresent(message = "有効期限は今日以降を指定してください")
    private LocalDate expiringDate;

    @NotNull(message = "受け取り手は必須です")
    @Min(value = 1, message = "受け取り手のIDが不正です")
    private Integer ownerId;
}
