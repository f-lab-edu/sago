package com.dhmall.auction.mapper;

import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.dto.ProductDto;
import com.dhmall.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Mapper
@Repository
public interface AuctionMapper {
    void insertChatMessage(ChatMessageDto chatMessage);

    UserDto selectUserById(BigInteger id);

    ProductDto selectProductByCode(String productCode);
}
