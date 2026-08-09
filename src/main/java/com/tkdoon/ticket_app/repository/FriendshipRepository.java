package com.tkdoon.ticket_app.repository;

import com.tkdoon.ticket_app.dto.FriendUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FriendshipRepository {

    void insertFriendship(int userId1, int userId2);

    List<FriendUserDto> selectFriendList(int userId);
}
