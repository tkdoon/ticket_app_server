package com.tkdoon.ticket_app.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TicketEntity {
    private int ticketId;
    private String title;
    private String description;
    private Timestamp expiringDate;
    private Boolean isUsed;
    private String creatorName;
    private String ownerName;
}
