package com.dhmall.auction.service;

import com.dhmall.auction.dto.ChatRoomDto;
import com.dhmall.auction.mapper.AuctionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final ObjectMapper objectMapper;
    private Map<BigInteger, ChatRoomDto> roomList = new HashMap<>();

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Batch 시스템 적용 전 테스트를 위한 코드(삭제 에정)
     */
    public void createAllAuctionRooms(List<String> rooms) {
        for(String id : rooms) {
            ChatRoomDto room = new ChatRoomDto();
            room.setId(new BigInteger(id));
            roomList.put(room.getId(), room);
        }
    }


    public ChatRoomDto joinAuction(BigInteger roomId) {
        return roomList.get(roomId);
    }

}