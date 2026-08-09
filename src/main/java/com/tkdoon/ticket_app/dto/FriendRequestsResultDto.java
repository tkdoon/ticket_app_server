package com.tkdoon.ticket_app.dto;

import lombok.Data;
import java.util.List;

@Data
public class FriendRequestsResultDto {
    private String result = "SUCCESS";
    private List<FriendUserDto> incoming;
    private List<FriendUserDto> outgoing;
}
