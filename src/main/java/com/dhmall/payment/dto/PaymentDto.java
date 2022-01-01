package com.dhmall.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Getter
@Setter
public class PaymentDto {
    private BigInteger id;

    private BigInteger userId;

    private String nickname;

    private String productCode;

    private String productName;

    private BigDecimal amount;

    private BigDecimal taxFreeAmount;

    private BigDecimal vatAmount;

    private String paymentCode;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
