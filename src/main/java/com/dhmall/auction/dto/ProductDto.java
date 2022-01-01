package com.dhmall.auction.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {

    private BigInteger id;

    private String code;

    private String name;

    private String category;

    private String owner;

    private int auctionStatus;

    private String description;

    private String createdAt;

    private String updatedAt;

}