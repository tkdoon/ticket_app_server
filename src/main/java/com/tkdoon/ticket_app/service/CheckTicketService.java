package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.CheckTicketResultDto;
import com.tkdoon.ticket_app.entity.TicketEntity;
import com.tkdoon.ticket_app.repository.TicketRepository;
import com.tkdoon.ticket_app.security.AuthContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CheckTicketService {

    private final TicketRepository ticketRepository;

    public CheckTicketService(TicketRepository ticketRepository){
        this.ticketRepository=ticketRepository;
    }

    public CheckTicketResultDto checkTicket(){
        int userId = Objects.requireNonNull(AuthContext.currentUser()).getId();
        List<TicketEntity> receivedTicketList = ticketRepository.selectTicketsByOwnerId(userId);
        List<TicketEntity> sentTicketList = ticketRepository.selectSentTicketsByCreatorId(userId);

        CheckTicketResultDto resultEntity = new CheckTicketResultDto();
        resultEntity.setReceivedTicketList(receivedTicketList);
        resultEntity.setSentTicketList(sentTicketList);
        return resultEntity;
    }
}
