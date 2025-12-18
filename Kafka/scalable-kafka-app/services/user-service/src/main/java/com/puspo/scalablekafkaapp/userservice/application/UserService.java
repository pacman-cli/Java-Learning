package com.puspo.scalablekafkaapp.userservice.application;

import com.puspo.scalablekafkaapp.userservice.domain.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUserById(Long id);

}
