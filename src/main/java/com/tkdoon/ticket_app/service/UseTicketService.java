package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.UseResultDto;
import com.tkdoon.ticket_app.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class UseTicketService {
    private final TicketRepository ticketRepository;
    public UseTicketService(TicketRepository ticketRepository){
        this.ticketRepository=ticketRepository;
    }

    public UseResultDto useTicket(int ticketId){
        int updatedRowCount=ticketRepository.updateTicketUse(ticketId);
        UseResultDto useResultDto=new UseResultDto();
        if(updatedRowCount==0){
            useResultDto.setResult("ERROR");
            useResultDto.setMessage("使用可能な券がありません。");
        }
        return useResultDto;
    }
}
