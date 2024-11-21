package com.kubuski.urlshortener.job;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;

@Component
public class UrlCleanupJob extends QuartzJobBean {
    private final UrlRepository urlRepository;

    @Value("${urlentity.lifetime.days}")
    private int entityLifetimeDays;

    public UrlCleanupJob(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Instant cutoffDate = Instant.now().minus(entityLifetimeDays, ChronoUnit.DAYS);
        List<Url> urlsToDelete = urlRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);
        urlRepository.deleteAll(urlsToDelete);
    }
}
