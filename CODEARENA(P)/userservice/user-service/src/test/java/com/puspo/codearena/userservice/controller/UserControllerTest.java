package com.puspo.codearena.userservice.controller;

import com.puspo.codearena.userservice.user.profile.controller.UserController;
import com.puspo.codearena.userservice.user.profile.dto.UserDto;
import com.puspo.codearena.userservice.user.profile.entity.User;
import com.puspo.codearena.userservice.user.profile.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserById_Success() {
        UUID userId = UUID.randomUUID();
        // FIX: Set id on User instance to match test userId
        User user = User.builder()
                .id(userId)
                .build();

        //mocking
        when(userService.getUserById(userId)).thenReturn(UserDto.toDto(user));

        //perform the test
        ResponseEntity<UserDto> response = userController.getUserById(userId);

        //assertions{expected return value, actual return value}
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDto user1 = response.getBody();
        assertEquals(userId, user1.getId());
    }
}
