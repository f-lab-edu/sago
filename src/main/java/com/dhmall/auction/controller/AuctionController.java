package com.dhmall.auction.controller;

import com.dhmall.auction.dto.AuctionListDto;
import com.dhmall.auction.dto.ChatMessageDto;
import com.dhmall.auction.service.AuctionService;
import com.dhmall.config.RedisConfig;
import com.dhmall.util.SagoApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auctions/")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;
    private final RedisConfig redisConfig;

    // TODO: batch 시스템 기능 구현
    public SagoApiResponse<List<AuctionListDto>> auctionList() {
        return null;
    }

    /**
     * batch 시스템 적용 전 코드(삭제 예정)
     */
    @PostMapping("chatRoom")
    public SagoApiResponse<String> createAuctionRooms() {
        List<String> roomIds = new ArrayList<>();
        roomIds.add("1");
        roomIds.add("2");
        roomIds.add("3");
        auctionService.createAllAuctionRooms(roomIds);

        ValueOperations<String, Object> test = redisConfig.redisTemplate(redisConfig.redisRepositoryFactory()).opsForValue();
        test.set(String.valueOf(1), "test");

        return new SagoApiResponse<>(HttpStatus.ACCEPTED, "온라인 경매방이 생성되었습니다.");
    }

    // TODO: 이전 메시지 입장하는 사용자에게 최신 10개 보여주기
    @GetMapping("joinAuction")
    public SagoApiResponse<List<ChatMessageDto>> joinAuction(@NonNull BigInteger roomId) {
        return null;
    }
}