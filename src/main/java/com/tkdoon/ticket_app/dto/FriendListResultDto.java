package com.tkdoon.ticket_app.dto;

import lombok.Data;
import java.util.List;

@Data
public class FriendListResultDto {
    private String result = "SUCCESS";
    private List<FriendUserDto> friends;
}
