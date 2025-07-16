package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.CheckTicketResultDto;
import com.tkdoon.ticket_app.service.CheckTicketService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class CheckTicketController {
    private final CheckTicketService checkTicketService;

    public CheckTicketController(CheckTicketService checkTicketService){
        this.checkTicketService=checkTicketService;
    }


    @RequestMapping(value="/check",method = RequestMethod.GET)
    public CheckTicketResultDto checkTicket(){

        return checkTicketService.checkTicket();
    }

}
