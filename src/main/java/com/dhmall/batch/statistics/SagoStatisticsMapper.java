package com.dhmall.batch.statistics;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Mapper
@Repository
public interface SagoStatisticsMapper {
    SagoStatisticsDto selectCountFromTables(ZonedDateTime createdAt);
    void insertSagoStatisticsInfo(SagoStatisticsDto sagoStatisticsDto);
}
