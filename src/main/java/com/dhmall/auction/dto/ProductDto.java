package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@Getter
@RequiredArgsConstructor
public class ProductDto {

    private final BigInteger id;

    private final String code;

    private final String name;

    private final String category;

    private final String owner;

    private final int auctionStatus;

    private final String description;

    private final String createdAt;

    private final String updatedAt;

}