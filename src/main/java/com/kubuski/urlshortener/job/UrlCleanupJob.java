package com.kubuski.urlshortener.job;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;

@Component
public class UrlCleanupJob extends QuartzJobBean {
    private final UrlRepository urlRepository;
    private final int ENTITY_LIFETIME_DAYS = 5;

    public UrlCleanupJob(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Instant cutoffDate = Instant.now().minus(ENTITY_LIFETIME_DAYS, ChronoUnit.DAYS);
        List<Url> urlsToDelete = urlRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);
        urlRepository.deleteAll(urlsToDelete);
    }
}
