package com.tkdoon.ticket_app.repository;

import com.tkdoon.ticket_app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepository {

    List<UserEntity> selectExistingUser(String email);

    void insertUser(String email,String userName,String iconUrl);

    void updateUserName(int id, String userName);

    UserEntity selectUserById(int id);

}
