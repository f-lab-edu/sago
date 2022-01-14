package com.dhmall.batch.auction;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuctionRoomsJob implements Job {

    @Autowired
    private AuctionRoomsJobService auctionRoomsJobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        auctionRoomsJobService.executeJob();
    }
}
