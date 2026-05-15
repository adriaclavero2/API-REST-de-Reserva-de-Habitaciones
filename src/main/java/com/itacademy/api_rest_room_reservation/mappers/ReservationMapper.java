package com.itacademy.api_rest_room_reservation.mappers;

import com.itacademy.api_rest_room_reservation.entities.Reservation;
import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.entities.User;
import com.itacademy.api_rest_room_reservation.enums.ReservationStatus;
import com.itacademy.api_rest_room_reservation.requestDTOS.ReservationRequestDTO;
import responseDTOS.ReservationResponseDTO;
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

    public Reservation toEntity(ReservationRequestDTO dto, User user, Room room) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setRoom(room);

        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservation;
    }
}
