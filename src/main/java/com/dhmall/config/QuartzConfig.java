package com.dhmall.config;

import com.dhmall.batch.auction.AuctionRoomsJob;
import com.dhmall.batch.statistics.SagoStatisticsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class QuartzConfig {

    private final ApplicationContext applicationContext;
    private final DataSource quartzDataSource;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {

        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean sagoAuctionRoomScheduler(@Qualifier("sagoAuctionRoomTrigger") Trigger trigger,
                                                         @Qualifier("sagoAuctionRoomJobDetail") JobDetail job) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        log.debug("Setting auction room scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        // quartz job 스케쥴링 정보 MySQL DB 저장
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean
    public JobDetailFactoryBean sagoAuctionRoomJobDetail() {

        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(AuctionRoomsJob.class);
        jobDetailFactory.setName("Sago_Auction_Room_Job_Detail");
        jobDetailFactory.setDescription("Invoke Auction Room Job service...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean sagoAuctionRoomTrigger(@Qualifier("sagoAuctionRoomJobDetail") JobDetail job) {

        CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
        cronTrigger.setJobDetail(job);
        cronTrigger.setName("Sago_Auction_Room_Cron_Trigger");
        cronTrigger.setDescription("Invoke Auction Room Cron Trigger...");
        // 매일 새벽 12시(0시)에 배치 작업 시작
        cronTrigger.setCronExpression("0 0 * * * ?");

        return cronTrigger;
    }

    @Bean
    public SchedulerFactoryBean sagoStatisticsScheduler(@Qualifier("sagoStatisticsTrigger") Trigger trigger,
                                                        @Qualifier("sagoStatisticsJobDetail") JobDetail job) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        log.debug("Setting statistics scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        // quartz job 스케쥴링 정보 MySQL DB 저장
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    @Bean
    public JobDetailFactoryBean sagoStatisticsJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(SagoStatisticsJob.class);
        jobDetailFactory.setName("Sago_Statistics_Job_Detail");
        jobDetailFactory.setDescription("Invoke Statistics Job service...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean sagoStatisticsTrigger(@Qualifier("sagoStatisticsJobDetail") JobDetail job) {

        CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
        cronTrigger.setJobDetail(job);
        cronTrigger.setName("Sago_Statistics_Cron_Trigger");
        cronTrigger.setDescription("Invoke Statistics Cron Trigger...");
        // 매일 새벽 12시(0시)에 배치 작업 시작
        cronTrigger.setCronExpression("0 0 * * * ?");

        return cronTrigger;
    }
}