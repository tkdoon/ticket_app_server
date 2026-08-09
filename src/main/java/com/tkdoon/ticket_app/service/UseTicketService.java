package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.UseResultDto;
import com.tkdoon.ticket_app.repository.TicketRepository;
import com.tkdoon.ticket_app.security.AuthContext;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UseTicketService {
    private final TicketRepository ticketRepository;
    public UseTicketService(TicketRepository ticketRepository){
        this.ticketRepository=ticketRepository;
    }

    public UseResultDto useTicket(int ticketId){
        int ownerId = Objects.requireNonNull(AuthContext.currentUser()).getId();
        int updatedRowCount=ticketRepository.updateTicketUse(ticketId, ownerId);
        UseResultDto useResultDto=new UseResultDto();
        if(updatedRowCount==0){
            useResultDto.setResult("ERROR");
            useResultDto.setMessage("使用可能な券がありません。");
        }
        return useResultDto;
    }
}
