package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatRoomDto implements Serializable {

    @Id
    private String id;

    private String productCode;

    private String productName;
}