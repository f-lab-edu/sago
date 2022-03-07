package com.dhmall.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TossPayDto {

    private String paymentKey;

    private BigDecimal amount;

    private BigDecimal taxFreeAmount;

    private BigDecimal vatAmount;

    private String message;
}
