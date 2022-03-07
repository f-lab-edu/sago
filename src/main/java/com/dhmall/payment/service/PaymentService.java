package com.dhmall.payment.service;

import com.dhmall.auction.dto.AuctionDto;
import com.dhmall.auction.dto.AuctionWinnerDto;
import com.dhmall.auction.dto.ProductDto;
import com.dhmall.auction.service.AuctionService;
import com.dhmall.payment.dto.TossPayDto;
import com.dhmall.payment.mapper.PaymentMapper;
import com.dhmall.user.dto.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final AuctionService auctionService;
    private final RedisTemplate redisTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${toss.secret}")
    private final String TOSS_SECRET_KEY;

    public TossPayDto approvePayment(String paymentKey, BigDecimal amount, String orderId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
         headers.setBasicAuth(TOSS_SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {

            JsonNode body = responseEntity.getBody();

            TossPayDto payResult = TossPayDto.builder()
                    .paymentKey(body.get("paymentKey").asText())
                    .amount(new BigDecimal(body.get("totalAmount").asText()))
                    // TEST api key로는 해당 index json response 값이 존재하지 않음(null).
//                    .taxFreeAmount(new BigDecimal(body.get("suppliedAmount").asText()))
//                    .vatAmount(new BigDecimal(body.get("vat").asText()))
                    .message("결제가 성공적으로 이루어졌습니다.")
                    .build();

            return payResult;
        }

        TossPayDto payResult = TossPayDto.builder()
                .paymentKey("")
                .amount(new BigDecimal(0))
                .taxFreeAmount(new BigDecimal(0))
                .vatAmount(new BigDecimal(0))
                .message("결제 실패하였습니다. 다시 시도해주세요.")
                .build();

        return payResult;
    }

    public AuctionDto confirmAuction(String chatRoomId) {
        // TODO: Auction 채팅방에서 Redis에다가 userId, productCode, amount 저장하기
        AuctionWinnerDto winner = (AuctionWinnerDto) redisTemplate.opsForValue().get(chatRoomId);

        UserDto user = auctionService.getAuctionWinner(new BigInteger(winner.getUserId()));
        ProductDto product = auctionService.getAuctionProduct(winner.getProductCode());

        AuctionDto auctionInfo = new AuctionDto();
        auctionInfo.setUserId(user.getId());
        auctionInfo.setNickname(user.getNickname());
        auctionInfo.setProductCode(product.getCode());
        auctionInfo.setProductName(product.getName());
        auctionInfo.setAmount(new BigDecimal(winner.getAmount()));
        auctionInfo.setDescription(product.getDescription());
        auctionInfo.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        auctionInfo.setUpdatedAt(ZonedDateTime.now(ZoneId.of("UTC")));

        return auctionInfo;
    }
}
