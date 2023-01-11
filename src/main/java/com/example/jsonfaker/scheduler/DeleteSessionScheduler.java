package com.example.jsonfaker.scheduler;

import com.example.jsonfaker.model.MfaLoginSession;
import com.example.jsonfaker.repository.MfaLoginSessionRepository;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

        List<MfaLoginSession> expiredSessions = mfaLoginSessionRepository.findAll()
                .stream()
                .filter(currentSession -> Duration.between(currentSession.getCreatedDate(),Instant.now()).getSeconds() > 30)
                .toList();
        mfaLoginSessionRepository.deleteAll(expiredSessions);
        logger.info("All expired mfa login sessions deleted at {}", Instant.now());
    }

}
