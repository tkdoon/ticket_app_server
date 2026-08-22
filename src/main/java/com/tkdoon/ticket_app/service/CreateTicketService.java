package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.CreateTicketRequestDto;
import com.tkdoon.ticket_app.dto.CreateTicketResultDto;
import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.repository.TicketRepository;
import com.tkdoon.ticket_app.repository.UserRepository;
import com.tkdoon.ticket_app.security.AuthContext;
import com.tkdoon.ticket_app.security.AuthUser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class CreateTicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public CreateTicketService(TicketRepository ticketRepository,
                               UserRepository userRepository,
                               MailService mailService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public CreateTicketResultDto createTicket(CreateTicketRequestDto request) {
        AuthUser creator = Objects.requireNonNull(AuthContext.currentUser());
        Timestamp expiringDate = Timestamp.valueOf(request.getExpiringDate().atTime(LocalTime.MAX));

        ticketRepository.insertTicket(
                request.getTitle(),
                request.getDescription(),
                expiringDate,
                request.getOwnerId(),
                creator.getId()
        );

        UserEntity owner = userRepository.selectUserById(request.getOwnerId());
        UserEntity creatorEntity = userRepository.selectUserById(creator.getId());
        if (owner != null && creatorEntity != null) {
            mailService.sendTicketNotification(
                    owner.getEmail(),
                    owner.getUserName(),
                    creatorEntity.getUserName(),
                    request.getTitle(),
                    request.getDescription(),
                    request.getExpiringDate()
            );
        }

        return new CreateTicketResultDto();
    }
}
