package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(int userId) {
        return userRepository.selectUserById(userId);
    }

    public void updateUserName(int userId, String userName) {
        userRepository.updateUserName(userId, userName);
    }
}
