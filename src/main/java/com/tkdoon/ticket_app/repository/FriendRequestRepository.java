package com.tkdoon.ticket_app.repository;

import com.tkdoon.ticket_app.dto.FriendUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FriendRequestRepository {

    void insertFriendRequest(int requesterId, int addresseeId);

    int deleteFriendRequest(int requesterId, int addresseeId);

    List<FriendUserDto> selectIncomingRequests(int userId);

    List<FriendUserDto> selectOutgoingRequests(int userId);
}
