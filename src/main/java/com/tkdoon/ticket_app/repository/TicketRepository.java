package com.tkdoon.ticket_app.repository;


import com.tkdoon.ticket_app.entity.TicketEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Mapper
public interface TicketRepository {
    List<TicketEntity> selectTicketsByOwnerId(@Param("ownerId") int ownerId);

    int updateTicketUse(@Param("ticketId")int ticketId);

    void insertTicket(@Param("title") String title,
                      @Param("description") String description,
                      @Param("expiringDate") Timestamp expiringDate,
                      @Param("ownerId") int ownerId,
                      @Param("creatorId") int creatorId);
}
