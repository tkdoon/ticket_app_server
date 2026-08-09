package com.tkdoon.ticket_app.service;

import com.tkdoon.ticket_app.dto.FriendListResultDto;
import com.tkdoon.ticket_app.dto.FriendRequestsResultDto;
import com.tkdoon.ticket_app.repository.FriendRequestRepository;
import com.tkdoon.ticket_app.repository.FriendshipRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendService(FriendRequestRepository friendRequestRepository,
                         FriendshipRepository friendshipRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void sendFriendRequest(int requesterId, int addresseeId) {
        friendRequestRepository.insertFriendRequest(requesterId, addresseeId);
    }

    public FriendRequestsResultDto getFriendRequests(int userId) {
        FriendRequestsResultDto dto = new FriendRequestsResultDto();
        dto.setIncoming(friendRequestRepository.selectIncomingRequests(userId));
        dto.setOutgoing(friendRequestRepository.selectOutgoingRequests(userId));
        return dto;
    }

    @Transactional
    public void acceptFriendRequest(int requesterId, int addresseeId) {
        int deleted = friendRequestRepository.deleteFriendRequest(requesterId, addresseeId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該当する友達申請がありません");
        }
        friendshipRepository.insertFriendship(requesterId, addresseeId);
    }

    public FriendListResultDto getFriendList(int userId) {
        FriendListResultDto dto = new FriendListResultDto();
        dto.setFriends(friendshipRepository.selectFriendList(userId));
        return dto;
    }
}
