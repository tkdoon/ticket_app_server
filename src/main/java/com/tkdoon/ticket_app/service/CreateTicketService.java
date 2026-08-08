package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.CreateTicketRequestDto;
import com.tkdoon.ticket_app.dto.CreateTicketResultDto;
import com.tkdoon.ticket_app.repository.TicketRepository;
import com.tkdoon.ticket_app.security.AuthContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class CreateTicketService {

    private final TicketRepository ticketRepository;

    public CreateTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public CreateTicketResultDto createTicket(CreateTicketRequestDto request) {
        int creatorId = Objects.requireNonNull(AuthContext.currentUser()).getId();
        Timestamp expiringDate = Timestamp.valueOf(request.getExpiringDate().atTime(LocalTime.MAX));

        ticketRepository.insertTicket(
                request.getTitle(),
                request.getDescription(),
                expiringDate,
                request.getOwnerId(),
                creatorId
        );

        return new CreateTicketResultDto();
    }
}
