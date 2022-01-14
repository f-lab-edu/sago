package com.dhmall.batch.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class SagoStatisticsJobService {

    private final SagoStatisticsMapper sagoStatisticsMapper;

    public void executeJob() {
        SagoStatisticsDto statisticsInfo = findCountFromTables();
        createSagoStatisticsInfo(statisticsInfo);
    }

    public SagoStatisticsDto findCountFromTables() {
        ZonedDateTime yesterday = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(1);
        return sagoStatisticsMapper.selectCountFromTables(yesterday);
    }

    @Transactional
    public void createSagoStatisticsInfo(SagoStatisticsDto sagoStatisticsInfo) {
        sagoStatisticsInfo.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        sagoStatisticsMapper.insertSagoStatisticsInfo(sagoStatisticsInfo);
    }

}
