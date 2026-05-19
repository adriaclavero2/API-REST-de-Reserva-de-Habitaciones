package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.exceptions.ResourceNotFoundException;
import com.itacademy.api_rest_room_reservation.mappers.UserMapper;
import com.itacademy.api_rest_room_reservation.repositories.UserRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import responseDTOS.UserResponseDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnUser_WhenEmailDoesNotExist() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("email@email.com");

        User userEntity = new User();
        UserResponseDTO responseDTO = new UserResponseDTO();

        when(userRepository.existsByEmail("email@email.com")).thenReturn(false);
        when(userMapper.toEntity(requestDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void createUser_ShouldThrowConflictException_WhenEmailAlreadyExists() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("email@email.com");

        when(userRepository.existsByEmail("email@email.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            userService.createUser(requestDTO);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        Long fakeId = 99L;

        when(userRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(fakeId);
        });
    }
}