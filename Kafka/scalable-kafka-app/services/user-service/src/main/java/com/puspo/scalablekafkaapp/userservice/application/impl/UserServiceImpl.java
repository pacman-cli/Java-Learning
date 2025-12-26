package com.puspo.scalablekafkaapp.userservice.application.impl;

import com.puspo.scalablekafkaapp.userservice.application.UserService;
import com.puspo.scalablekafkaapp.userservice.domain.User;
import com.puspo.scalablekafkaapp.userservice.infrastructure.persistence.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;

    public UserServiceImpl(KafkaTemplate<String, String> kafkaTemplate, UserRepository userRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        User created = userRepository.save(user);
        kafkaTemplate.send("user.created", created.getId() + ":" + created.getEmail());
        return created;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        kafkaTemplate.send("user.deleted", id + "");
    }
}
