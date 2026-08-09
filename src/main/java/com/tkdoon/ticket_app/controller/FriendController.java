package com.tkdoon.ticket_app.controller;

import com.tkdoon.ticket_app.dto.*;
import com.tkdoon.ticket_app.security.AuthContext;
import com.tkdoon.ticket_app.security.AuthUser;
import com.tkdoon.ticket_app.service.FriendService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/request")
    public FriendActionResultDto sendRequest(@Valid @RequestBody SendFriendRequestDto request) {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        int myId = currentUser.getId();
        if (myId == request.getAddresseeId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "自分自身に友達申請はできません");
        }
        friendService.sendFriendRequest(myId, request.getAddresseeId());
        return new FriendActionResultDto();
    }

    @GetMapping("/requests")
    public FriendRequestsResultDto getRequests() {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return friendService.getFriendRequests(currentUser.getId());
    }

    @PostMapping("/accept")
    public FriendActionResultDto acceptRequest(@Valid @RequestBody AcceptFriendRequestDto request) {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        friendService.acceptFriendRequest(request.getRequesterId(), currentUser.getId());
        return new FriendActionResultDto();
    }

    @GetMapping("/list")
    public FriendListResultDto getFriendList() {
        AuthUser currentUser = AuthContext.currentUser();
        if (currentUser == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return friendService.getFriendList(currentUser.getId());
    }
}
