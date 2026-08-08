package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity login(String email, String userName, String iconUrl) {
        List<UserEntity> existing = userRepository.selectExistingUser(email);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        userRepository.insertUser(email, userName, iconUrl);
        return userRepository.selectExistingUser(email).get(0);
    }
}
