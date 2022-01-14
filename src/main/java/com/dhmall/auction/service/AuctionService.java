package com.dhmall.auction.service;

import com.dhmall.auction.RedisSubscriber;
import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.dto.ChatRoomDto;
import com.dhmall.auction.mapper.AuctionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;
    private final AuctionMapper auctionMapper;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    public List<ChatRoomDto> findAllAuctionRooms() {
        return auctionMapper.selectAllChatRooms();
    }

    @Transactional
    public void saveChatMessage(ChatMessageDto chatMessage) {
        chatMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        chatMessage.setUpdatedAt(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        auctionMapper.insertChatMessage(chatMessage);
    }

    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}