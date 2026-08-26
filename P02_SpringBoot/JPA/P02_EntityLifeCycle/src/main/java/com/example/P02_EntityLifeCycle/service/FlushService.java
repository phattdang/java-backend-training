package com.example.P02_EntityLifeCycle.service;

import com.example.P02_EntityLifeCycle.dto.FlushResult;
import com.example.P02_EntityLifeCycle.entity.User;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class FlushService {

    private static final Logger log = LoggerFactory.getLogger(FlushService.class);

    private final EntityManager entityManager;

    public FlushService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public FlushResult demonstrateFlush(Long id, String newName) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, "User not found: " + id);
        }

        user.setName(newName);
        log.info("Before entityManager.flush(): UPDATE has not been forced by this method yet");
        entityManager.flush(); // Synchronizes changes now; it does NOT commit the transaction.
        log.info("After entityManager.flush(): SQL was sent, but transaction commit follows method return");

        return new FlushResult(
                id, newName, true, false,
                "Find the UPDATE between the before/after flush log messages."
        );
    }
}
