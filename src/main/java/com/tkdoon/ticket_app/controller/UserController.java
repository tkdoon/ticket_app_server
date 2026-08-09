package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.UpdateUserNameRequestDto;
import com.tkdoon.ticket_app.dto.UpdateUserNameResultDto;
import com.tkdoon.ticket_app.dto.UserProfileResultDto;
import com.tkdoon.ticket_app.entity.UserEntity;
import com.tkdoon.ticket_app.security.AuthContext;
import com.tkdoon.ticket_app.security.AuthUser;
import com.tkdoon.ticket_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileResultDto getProfile() {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userService.getUserProfile(currentUser.getId());
        UserProfileResultDto dto = new UserProfileResultDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        return dto;
    }

    @GetMapping("/{id}")
    public UserProfileResultDto getUserById(@PathVariable int id) {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userService.getUserProfile(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ユーザーが見つかりません");
        }
        UserProfileResultDto dto = new UserProfileResultDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        return dto;
    }

    @PostMapping("/update-name")
    public UpdateUserNameResultDto updateName(@Valid @RequestBody UpdateUserNameRequestDto request) {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        userService.updateUserName(currentUser.getId(), request.getUserName());
        return new UpdateUserNameResultDto();
    }
}
