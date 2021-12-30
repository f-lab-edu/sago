package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Getter
@Setter
public class AuctionDto {

    @Id
    private BigInteger id;

    private BigInteger userId;

    private String nickname;

    private String productCode;

    private String productName;

    private BigDecimal amount;

    private String description;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

}
