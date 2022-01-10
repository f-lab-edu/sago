package com.dhmall.auction.mapper;

import com.dhmall.auction.dto.ChatMessageDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AuctionMapper {
    void insertChatMessage(ChatMessageDto chatMessage);
}
