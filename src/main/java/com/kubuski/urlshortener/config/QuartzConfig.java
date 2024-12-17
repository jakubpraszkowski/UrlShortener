package com.kubuski.urlshortener.config;

import com.kubuski.urlshortener.job.UrlCleanupJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    private final String CRON_EXPRESSION = "0 0 0 * * ?";
    private final String JOB_NAME = "urlCleanupJob";
    private final String TRIGGER_NAME = "urlCleanupTrigger";

    @Bean
    public JobDetail urlCleanupJobDetail() {
        return createJobDetail(UrlCleanupJob.class, JOB_NAME);
    }

    @Bean
    public Trigger urlCleanupJobTrigger() {
        return createTrigger(urlCleanupJobDetail(), TRIGGER_NAME, CRON_EXPRESSION);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobDetails(urlCleanupJobDetail());
        schedulerFactoryBean.setTriggers(urlCleanupJobTrigger());
        return schedulerFactoryBean;
    }

    private JobDetail createJobDetail(Class<? extends Job> jobClass, String jobName) {
        return JobBuilder.newJob(jobClass).withIdentity(jobName).storeDurably().build();
    }

    private Trigger createTrigger(JobDetail jobDetail, String triggerName, String cronExpression) {
        return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(triggerName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
    }
}
