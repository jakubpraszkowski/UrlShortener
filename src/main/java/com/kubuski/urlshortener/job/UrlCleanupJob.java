package com.kubuski.urlshortener.job;

import com.kubuski.urlshortener.entity.Url;
import com.kubuski.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UrlCleanupJob extends QuartzJobBean {
    private static final int ENTITY_LIFETIME_DAYS = 5;

    private final UrlRepository urlRepository;

    @Override
    protected void executeInternal(final JobExecutionContext context) {
        Instant cutoffDate = calculateCutoffDate();
        List<Url> urlsToDelete = findUrlsToDelete(cutoffDate);
        deleteUrls(urlsToDelete);
    }

    private Instant calculateCutoffDate() {
        return Instant.now().minus(ENTITY_LIFETIME_DAYS, ChronoUnit.DAYS);
    }

    private List<Url> findUrlsToDelete(Instant cutoffDate) {
        return urlRepository.findAllByDeletedTrueAndUpdatedAtBefore(cutoffDate);
    }

    private void deleteUrls(List<Url> urlsToDelete) {
        urlRepository.deleteAll(urlsToDelete);
    }
}
