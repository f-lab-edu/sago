package com.dhmall.batch.auction;

import com.dhmall.auction.dto.ProductDto;
import com.dhmall.auction.mapper.AuctionRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionRoomsJobService {

    private final AuctionRoomMapper auctionRoomMapper;

    public void executeJob() {
        deleteAllAuctionRooms();
        resetAuctionRoomId();
        List<ProductDto> products = findAllAuctionProducts();
        createAllAuctionRooms(products);
    }

    public List<ProductDto> findAllAuctionProducts() {
        return auctionRoomMapper.findAuctionProductsByAuctionStatus();
    }

    @Transactional
    public void createAllAuctionRooms(List<ProductDto> products) {
        products.stream().forEach(product -> auctionRoomMapper.insertAuctionRoom(product));
    }

    @Transactional
    public void deleteAllAuctionRooms() {
        auctionRoomMapper.deleteAuctionRoom();
    }

    @Transactional
    public void resetAuctionRoomId() {
        auctionRoomMapper.alterAuctionRoomId();
    }
}
