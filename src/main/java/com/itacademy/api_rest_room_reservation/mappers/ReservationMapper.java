package com.itacademy.api_rest_room_reservation.mappers;

import com.itacademy.api_rest_room_reservation.entities.Reservation;
import dtos.ReservationResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;

        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());
        return dto;
    }
}
