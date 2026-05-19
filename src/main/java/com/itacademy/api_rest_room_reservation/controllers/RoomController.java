package com.itacademy.api_rest_room_reservation.controllers;

import com.itacademy.api_rest_room_reservation.requestDTOS.RoomRequestDTO;
import com.itacademy.api_rest_room_reservation.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import responseDTOS.RoomResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@Valid @RequestBody RoomRequestDTO requestDTO) {
        RoomResponseDTO createdRoom = roomService.createRoom(requestDTO);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        RoomResponseDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }
}