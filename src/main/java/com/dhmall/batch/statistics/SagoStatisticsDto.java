package com.dhmall.batch.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Getter
@Setter
public class SagoStatisticsDto {
    BigInteger totalDayAuctionCount;
    BigInteger totalDayUserCount;
    BigInteger totalDayPaymentCount;
    ZonedDateTime createdAt;
}
