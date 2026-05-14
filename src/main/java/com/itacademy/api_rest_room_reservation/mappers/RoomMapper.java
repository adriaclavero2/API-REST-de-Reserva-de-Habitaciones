package com.itacademy.api_rest_room_reservation.mappers;

import com.itacademy.api_rest_room_reservation.entities.Room;
import dtos.RoomResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomResponseDTO toDTO(Room room) {
        if (room == null) return null;

        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        return dto;
    }
}
