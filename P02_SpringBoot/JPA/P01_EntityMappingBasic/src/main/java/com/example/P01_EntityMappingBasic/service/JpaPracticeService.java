package com.example.P01_EntityMappingBasic.service;

import com.example.P01_EntityMappingBasic.dto.CreateSequenceUserRequest;
import com.example.P01_EntityMappingBasic.dto.CreateUserRequest;
import com.example.P01_EntityMappingBasic.entity.SequenceUser;
import com.example.P01_EntityMappingBasic.entity.User;
import com.example.P01_EntityMappingBasic.repository.SequenceUserRepository;
import com.example.P01_EntityMappingBasic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class JpaPracticeService {

    private final UserRepository userRepository;
    private final SequenceUserRepository sequenceUserRepository;

    public JpaPracticeService(UserRepository userRepository, SequenceUserRepository sequenceUserRepository) {
        this.userRepository = userRepository;
        this.sequenceUserRepository = sequenceUserRepository;
    }

    public User createUser(CreateUserRequest request) {
        User user = new User(
                request.fullName(),
                request.email(),
                request.status(),
                request.role(),
                request.dateOfBirth(),
                request.active(),
                request.age()
        );
        user.setTemporaryDisplayName(request.temporaryDisplayName());
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found: " + id));
    }

    public SequenceUser createSequenceUser(CreateSequenceUserRequest request) {
        return sequenceUserRepository.save(new SequenceUser(request.name()));
    }
}
