package com.tkdoon.ticket_app.dto;

import com.tkdoon.ticket_app.entity.TicketEntity;
import lombok.Data;

import java.util.List;

@Data
public class CheckTicketResultDto {

    private String result="SUCCESS";

    private List<TicketEntity> ticketList;
}
