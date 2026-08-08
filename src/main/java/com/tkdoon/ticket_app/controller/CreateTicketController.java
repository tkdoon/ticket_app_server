package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.CreateTicketRequestDto;
import com.tkdoon.ticket_app.dto.CreateTicketResultDto;
import com.tkdoon.ticket_app.service.CreateTicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class CreateTicketController {

    private final CreateTicketService createTicketService;

    public CreateTicketController(CreateTicketService createTicketService) {
        this.createTicketService = createTicketService;
    }

    @PostMapping("/create")
    public CreateTicketResultDto createTicket(@Valid @RequestBody CreateTicketRequestDto request) {
        return createTicketService.createTicket(request);
    }
}
