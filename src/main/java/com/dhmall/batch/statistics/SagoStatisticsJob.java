package com.dhmall.batch.statistics;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SagoStatisticsJob implements Job {

    @Autowired
    private SagoStatisticsJobService sagoStatisticsJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        sagoStatisticsJobService.executeJob();
    }
}
