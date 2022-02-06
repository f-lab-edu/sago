package com.dhmall.payment.controller;

import com.dhmall.auction.dto.AuctionDto;
import com.dhmall.payment.dto.PaymentDto;
import com.dhmall.payment.dto.TossPayDto;
import com.dhmall.payment.service.PaymentService;
import com.dhmall.util.SagoApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments/")
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${redirect.url}")
    private final String serverUrl;

    @GetMapping("request")
    public SagoApiResponse<AuctionDto> requestPayment(@RequestParam String chatRoomId) {
        // TODO: Auction 채팅방에서 Redis에다가 userId, productCode, amount 저장하기
        AuctionDto auctionResult = paymentService.confirmAuction(chatRoomId);
        return new SagoApiResponse<>(HttpStatus.ACCEPTED, auctionResult);
    }

    @PostMapping("/redirect")
    @ResponseBody
    public String paySuccess(TossPayDto payResult) {
        // TODO: API 결과값 redirect 처리
        return "Success!";
    }

    @GetMapping("success")
    public ResponseEntity<TossPayDto> confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam BigDecimal amount) throws Exception {
        TossPayDto payResult = paymentService.approvePayment(paymentKey, amount, orderId);
        URI redirectUri = new URI( serverUrl + "payments/redirect");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(payResult, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("fail")
    public SagoApiResponse<String> failRequestPayment() {
        return new SagoApiResponse<>(HttpStatus.BAD_REQUEST, "결제요청이 실패하였습니다. 다시 시도해주세요.");
    }
}
