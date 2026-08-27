package com.example.P03_JpaRepository.service;

import com.example.P03_JpaRepository.dto.CreateUserRequest;
import com.example.P03_JpaRepository.dto.RepositoryOperationResult;
import com.example.P03_JpaRepository.dto.UpdateUserRequest;
import com.example.P03_JpaRepository.entity.User;
import com.example.P03_JpaRepository.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RepositoryFlushService {

    private static final Logger log = LoggerFactory.getLogger(RepositoryFlushService.class);

    private final UserRepository userRepository;

    public RepositoryFlushService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public RepositoryOperationResult flushUpdate(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        user.setName(request.name());
        user.setEmail(request.email());

        log.info("Before userRepository.flush(): the transaction is active and the entity is modified");
        userRepository.flush();
        log.info("After userRepository.flush(): SQL was synchronized, but the transaction has not committed yet");

        return new RepositoryOperationResult(
                "flush()",
                "Forces pending SQL before method return; it does not commit the transaction.",
                user
        );
    }

    @Transactional
    public RepositoryOperationResult saveAndFlush(CreateUserRequest request) {
        User user = new User(request.name(), request.email());

        log.info("Before userRepository.saveAndFlush(): the new entity has not been saved");
        User savedUser = userRepository.saveAndFlush(user);
        log.info("After userRepository.saveAndFlush(): SQL was synchronized, but the transaction has not committed yet");

        return new RepositoryOperationResult(
                "saveAndFlush()",
                "Saves and immediately flushes the entity; flushing is not the same as committing.",
                savedUser
        );
    }
}
