package com.dhmall.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentDto {
    private final BigInteger id;

    private final BigInteger userId;

    private final String nickname;

    private final String productCode;

    private final String productName;

    private final BigDecimal amount;

    private final BigDecimal taxFreeAmount;

    private final BigDecimal vatAmount;

    private final String paymentCode;

    private final ZonedDateTime createdAt;

    private final ZonedDateTime updatedAt;
}
