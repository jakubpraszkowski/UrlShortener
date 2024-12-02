package com.kubuski.urlshortener.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.kubuski.urlshortener.job.UrlCleanupJob;

@Configuration
public class QuartzConfig {

    private static final String CRON_EXPRESSION = "0 0 0 * * ?";

    @Bean
    public JobDetail urlCleanupJobDetail() {
        return JobBuilder.newJob(UrlCleanupJob.class).withIdentity("urlCleanupJob").storeDurably()
                .build();
    }

    @Bean
    public Trigger urlCleanupJobTrigger() {
        return TriggerBuilder.newTrigger().forJob(urlCleanupJobDetail())
                .withIdentity("urlCleanupTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION)).build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobDetails(urlCleanupJobDetail());
        schedulerFactoryBean.setTriggers(urlCleanupJobTrigger());

        return schedulerFactoryBean;
    }
}
