package com.dhmall.auction.mapper;

import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.dto.ChatRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctionMapper {
    void insertChatMessage(ChatMessageDto chatMessage);

    List<ChatRoomDto> selectAllChatRooms();
}
