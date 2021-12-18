package com.dhmall.auction.handler;

import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.dto.ChatRoomDto;
import com.dhmall.auction.service.AuctionService;
import com.dhmall.config.DataSourceTemplateGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final AuctionService auctionService;
    private final DataSourceTemplateGenerator templateGenerator;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();

        log.info(message.getPayload());

        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

        // TODO: 주기적으로 Redis 데이터, MySQL DB로 마이그레이션(Redis 데이터 삭제)[비동기]
        if(chatMessage.getType().equals(ChatMessageDto.MessageType.TALK) || chatMessage.getType().equals(ChatMessageDto.MessageType.BID)) {
            final ListOperations<String, ChatMessageDto> chatMessageList = templateGenerator.redis().opsForList();
            chatMessageList.leftPush(String.valueOf(chatMessage.getRoomId()), chatMessage);
//            log.info(chatMessageList.range(String.valueOf(chatMessage.getRoomId()), 1, 3).get(0).getMessage());
        }
        ChatRoomDto room = auctionService.joinAuction(chatMessage.getRoomId());
        room.handleActions(session, chatMessage, auctionService);
    }
}