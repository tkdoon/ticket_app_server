package com.tkdoon.ticket_app.repository;


import com.tkdoon.ticket_app.entity.TicketEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TicketRepository {
    List<TicketEntity> selectTickets();

    int updateTicketUse(int ticketId);

}
