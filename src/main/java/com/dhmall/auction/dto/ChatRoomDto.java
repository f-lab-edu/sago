package com.dhmall.auction.dto;

import com.dhmall.auction.service.AuctionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Slf4j
public class ChatRoomDto {
    @Id
    private BigInteger id;

    private Set<WebSocketSession> sessions = new HashSet<>();

    public void handleActions(WebSocketSession session, ChatMessageDto message, AuctionService auctionService) {
        if(message.getType().equals(ChatMessageDto.MessageType.ENTER)) {
            sessions.add(session);
            message.setMessage(message.getNickname() + "님이 입장하셨습니다.");
        }

        sendMessage(message.getMessage(), auctionService);
    }

    public <T> void sendMessage(T message, AuctionService auctionService) {
        sessions.parallelStream().forEach(session -> auctionService.sendMessage(session, message));
    }
}