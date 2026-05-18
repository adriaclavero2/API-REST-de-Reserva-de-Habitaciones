package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.exceptions.ResourceNotFoundException;
import com.itacademy.api_rest_room_reservation.mappers.RoomMapper;
import com.itacademy.api_rest_room_reservation.repositories.RoomRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.RoomRequestDTO;
import org.springframework.stereotype.Service;
import responseDTOS.RoomResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }
    
    public RoomResponseDTO createRoom(RoomRequestDTO requestDTO) {
        if (roomRepository.existsByRoomNumber(requestDTO.getRoomNumber())) {
            throw new ConflictException("Conflict: Room number already exists");
        }
        Room roomToSave = roomMapper.toEntity(requestDTO);
        Room savedRoom = roomRepository.save(roomToSave);
        return roomMapper.toDTO(savedRoom);
    }

    public List<RoomResponseDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RoomResponseDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: Room with id "
                        + id + " does not exist"));
        return roomMapper.toDTO(room);
    }
}
