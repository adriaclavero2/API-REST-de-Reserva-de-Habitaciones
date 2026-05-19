package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.Reservation;
import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.enums.ReservationStatus;
import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.exceptions.ResourceNotFoundException;
import com.itacademy.api_rest_room_reservation.mappers.ReservationMapper;
import com.itacademy.api_rest_room_reservation.repositories.ReservationRepository;
import com.itacademy.api_rest_room_reservation.repositories.RoomRepository;
import com.itacademy.api_rest_room_reservation.repositories.UserRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.ReservationRequestDTO;
import com.itacademy.api_rest_room_reservation.validators.ReservationValidator;
import org.springframework.stereotype.Service;
import responseDTOS.ReservationResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationValidator validator;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository,
                              RoomRepository roomRepository, ReservationMapper reservationMapper, ReservationValidator reservationValidator) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
        this.validator = reservationValidator;
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO dto) {
        validator.validateDates(dto.getStartDate(), dto.getEndDate());
        validator.validateRoomAvailability(dto.getRoomId(), dto.getStartDate(), dto.getEndDate());
        validator.validateUserAvailability(dto.getUserId(), dto.getStartDate(), dto.getEndDate());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: User not found"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: Room not found"));

        Reservation reservation = reservationMapper.toEntity(dto, user, room);
        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(savedReservation);
    }

    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ConflictException("Conflict: Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getReservationsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not Found: User not found");
        }
        return reservationRepository.findByUserId(userId).stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }
}
