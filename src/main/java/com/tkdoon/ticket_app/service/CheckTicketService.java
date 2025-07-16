package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.CheckTicketResultDto;
import com.tkdoon.ticket_app.entity.TicketEntity;
import com.tkdoon.ticket_app.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckTicketService {

    private final TicketRepository ticketRepository;

    public CheckTicketService(TicketRepository ticketRepository){
        this.ticketRepository=ticketRepository;
    }

    public CheckTicketResultDto checkTicket(){

    //        List<TicketEntity> ticketList=new ArrayList<TicketEntity>();
//
//        TicketEntity sampleTicket=new TicketEntity();
//        sampleTicket.setTitle("sample");
//        ticketList.add(sampleTicket);

    List<TicketEntity> ticketList=ticketRepository.selectTickets();

    CheckTicketResultDto resultEntity=new CheckTicketResultDto();
        resultEntity.setTicketList(ticketList);
    return resultEntity;
    }
}
