package com.example.jsonfaker.scheduler;

import com.example.jsonfaker.repository.MfaLoginSessionRepository;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DeleteSessionScheduler {

    private final Logger logger;
    private final MfaLoginSessionRepository mfaLoginSessionRepository;

    public DeleteSessionScheduler(Logger logger, MfaLoginSessionRepository mfaLoginSessionRepository) {
        this.logger = logger;
        this.mfaLoginSessionRepository = mfaLoginSessionRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void deleteSessionsFromDb(){
        mfaLoginSessionRepository.deleteAll();
        logger.info("All mfa login sessions deleted at {}", Instant.now());
    }

}
