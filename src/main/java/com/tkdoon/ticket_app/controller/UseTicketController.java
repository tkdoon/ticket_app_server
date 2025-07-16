package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.UseRequestDto;
import com.tkdoon.ticket_app.dto.UseResultDto;
import com.tkdoon.ticket_app.service.UseTicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class UseTicketController {
    private final UseTicketService useTicketService;

    public UseTicketController(UseTicketService useTicketService){
        this.useTicketService=useTicketService;
    }

    @RequestMapping(value="/use",method = RequestMethod.POST)
    public UseResultDto useTicket(@RequestBody UseRequestDto request){
    return useTicketService.useTicket(request.getTicketId());
    }
}
