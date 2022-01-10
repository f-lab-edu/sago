package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, BID
    }

    private MessageType type;

    @Id
    private String id;

    private String roomId;

    private String userId;

    private String nickname;

    private String message;

    private String createdAt;

    private String updatedAt;
}