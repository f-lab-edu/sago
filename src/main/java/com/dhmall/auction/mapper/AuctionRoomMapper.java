package com.dhmall.auction.mapper;

import com.dhmall.auction.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuctionRoomMapper {
    List<ProductDto> findAuctionProductsByAuctionStatus();

    void insertAuctionRoom(ProductDto auctionProduct);

    void deleteAuctionRoom();

    void alterAuctionRoomId();
}
