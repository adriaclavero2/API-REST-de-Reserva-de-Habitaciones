package com.itacademy.api_rest_room_reservation.validators;

import com.itacademy.api_rest_room_reservation.exceptions.ConflictException;
import com.itacademy.api_rest_room_reservation.repositories.ReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationValidator {

    private final ReservationRepository repository;

    public ReservationValidator(ReservationRepository repository) {
        this.repository = repository;
    }

    public void validateDates(LocalDate start, LocalDate end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Bad Request: Start date must be before end date");
        }
    }

    public void validateRoomAvailability(Long roomId, LocalDate start, LocalDate end) {
        if (repository.existsRoomOverlap(roomId, start, end)) {
            throw new ConflictException("Conflict: Room is already occupied for these dates");
        }
    }

    public void validateUserAvailability(Long userId, LocalDate start, LocalDate end) {
        if (repository.existsUserOverlap(userId, start, end)) {
            throw new ConflictException("Conflict: User already has another active reservation on these dates");
        }
    }
}