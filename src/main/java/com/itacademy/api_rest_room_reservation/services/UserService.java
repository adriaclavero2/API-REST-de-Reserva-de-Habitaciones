package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.mappers.UserMapper;
import com.itacademy.api_rest_room_reservation.repositories.UserRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.UserRequestDTO;
import org.springframework.stereotype.Service;
import responseDTOS.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Conflict: Email already in use");
        }
        User userToSave = userMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(userToSave);
        return userMapper.toDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found: User with id "
                        + id + " does not exist"));
        return userMapper.toDTO(user);
    }
}
