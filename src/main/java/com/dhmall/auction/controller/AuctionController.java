package com.dhmall.auction.controller;

import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.dto.ChatRoomDto;
import com.dhmall.auction.dto.MessageType;
import com.dhmall.auction.RedisPublisher;
import com.dhmall.auction.service.AuctionService;
import com.dhmall.util.SagoApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auctions/")
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final AuctionService auctionService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/auctions/message")
    public void message(ChatMessageDto message) {
        if (MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getNickname() + "님이 입장하셨습니다.");
            auctionService.enterChatRoom(message.getRoomId());
        } else {
            auctionService.saveChatMessage(message);
        }
        redisPublisher.publish(auctionService.getTopic(message.getRoomId()), message);
    }

    @GetMapping("auctionRoomList")
    @ResponseBody
    public SagoApiResponse<List<ChatRoomDto>> uploadAllAuctionRooms() {
        List<ChatRoomDto> availableAuctions = auctionService.findAllAuctionRooms();
        return new SagoApiResponse<>(HttpStatus.OK, availableAuctions);
    }
}