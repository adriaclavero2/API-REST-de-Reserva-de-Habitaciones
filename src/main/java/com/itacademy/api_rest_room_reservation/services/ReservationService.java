package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.mappers.ReservationMapper;
import com.itacademy.api_rest_room_reservation.repositories.ReservationRepository;
import com.itacademy.api_rest_room_reservation.repositories.RoomRepository;
import com.itacademy.api_rest_room_reservation.repositories.UserRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.ReservationRequestDTO;
import org.springframework.stereotype.Service;
import responseDTOS.ReservationResponseDTO;

@Service
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository,
                              RoomRepository roomRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        if (!dto.getStartDate().isBefore(dto.getEndDate())) {
            throw new RuntimeException("Bad Request: Start date must be before en date");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Not Found: User not found"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Not Found: Room not found"));

        if (reservationRepository.existsRoomOverlap())
    }
}
