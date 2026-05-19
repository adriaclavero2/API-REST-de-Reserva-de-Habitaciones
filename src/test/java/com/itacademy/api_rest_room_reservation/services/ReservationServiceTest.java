package com.itacademy.api_rest_room_reservation.services;

import com.itacademy.api_rest_room_reservation.entities.Reservation;
import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.enums.ReservationStatus;
import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.mappers.ReservationMapper;
import com.itacademy.api_rest_room_reservation.repositories.ReservationRepository;
import com.itacademy.api_rest_room_reservation.repositories.RoomRepository;
import com.itacademy.api_rest_room_reservation.repositories.UserRepository;
import com.itacademy.api_rest_room_reservation.requestDTOS.ReservationRequestDTO;
import responseDTOS.ReservationResponseDTO;
import com.itacademy.api_rest_room_reservation.validators.ReservationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock private ReservationRepository reservationRepository;
    @Mock private UserRepository userRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private ReservationMapper reservationMapper;
    @Mock private ReservationValidator validator;

    @InjectMocks
    private ReservationService reservationService;

    private ReservationRequestDTO requestDTO;
    private Reservation reservationEntity;

    @BeforeEach
    void setUp() {
        requestDTO = new ReservationRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setRoomId(101L);
        requestDTO.setStartDate(LocalDate.of(2026, 6, 10));
        requestDTO.setEndDate(LocalDate.of(2026, 6, 15));

        reservationEntity = new Reservation();
        reservationEntity.setId(1L);
        reservationEntity.setStatus(ReservationStatus.ACTIVE);
    }

    @Test
    void createReservation_ShouldReturnReservation_WhenAllIsValid() {
        User user = new User();
        Room room = new Room();
        ReservationResponseDTO responseDTO = new ReservationResponseDTO();

        doNothing().when(validator).validateDates(any(), any());
        doNothing().when(validator).validateRoomAvailability(any(), any(), any());
        doNothing().when(validator).validateUserAvailability(any(), any(), any());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(101L)).thenReturn(Optional.of(room));
        when(reservationMapper.toEntity(requestDTO, user, room)).thenReturn(reservationEntity);
        when(reservationRepository.save(reservationEntity)).thenReturn(reservationEntity);
        when(reservationMapper.toDTO(reservationEntity)).thenReturn(responseDTO);

        ReservationResponseDTO result = reservationService.createReservation(requestDTO);

        assertNotNull(result);
        verify(reservationRepository, times(1)).save(reservationEntity);
    }

    @Test
    void createReservation_ShouldThrowIllegalArgumentException_WhenDatesAreInvalid() {
        doThrow(new IllegalArgumentException("Invalid dates"))
                .when(validator).validateDates(any(), any());

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(requestDTO);
        });

        verify(userRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_ShouldThrowConflictException_WhenRoomIsAlreadyReserved() {
        doThrow(new ConflictException("Room busy"))
                .when(validator).validateRoomAvailability(any(), any(), any());

        assertThrows(ConflictException.class, () -> {
            reservationService.createReservation(requestDTO);
        });

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void createReservation_ShouldThrowConflictException_WhenUserHasOverlappingReservation() {
        doThrow(new ConflictException("User has another reservation"))
                .when(validator).validateUserAvailability(any(), any(), any());

        assertThrows(ConflictException.class, () -> {
            reservationService.createReservation(requestDTO);
        });

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservation_ShouldChangeStatusToCancelled_WhenReservationIsActive() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservationEntity));

        reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED, reservationEntity.getStatus());
        assertNotNull(reservationEntity.getCancelledAt());
        verify(reservationRepository, times(1)).save(reservationEntity);
    }

    @Test
    void cancelReservation_ShouldThrowConflictException_WhenReservationIsAlreadyCancelled() {
        reservationEntity.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservationEntity));

        assertThrows(ConflictException.class, () -> {
            reservationService.cancelReservation(1L);
        });

        verify(reservationRepository, never()).save(any());
    }
}