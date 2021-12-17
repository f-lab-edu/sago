package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK, BID
    }

    private MessageType type;

    @Id
    private BigInteger id;

    private BigInteger roomId;

    private BigInteger userId;

    private String nickname;

    private String message;

    ZonedDateTime createdAt;

    ZonedDateTime updatedAt;
}