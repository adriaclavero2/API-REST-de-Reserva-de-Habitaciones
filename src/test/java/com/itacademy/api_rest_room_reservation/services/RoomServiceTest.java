package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.mappers.RoomMapper;
import com.itacademy.api_rest_room_reservation.repositories.RoomRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.RoomRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void createRoom_ShouldThrowConflictException_WhenRoomNumberAlreadyExists() {
        RoomRequestDTO requestDTO = new RoomRequestDTO();
        requestDTO.setRoomNumber(101);

        when(roomRepository.existsByRoomNumber(101)).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            roomService.createRoom(requestDTO);
        });
        verify(roomRepository, never()).save(any());
    }
}