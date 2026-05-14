package com.itacademy.api_rest_room_reservation.mappers;

import com.itacademy.api_rest_room_reservation.entities.Room;
import com.itacademy.api_rest_room_reservation.requestDTOS.RoomRequestDTO;
import responseDTOS.RoomResponseDTO;
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

    public Room toEntity(RoomRequestDTO dto) {
        if (dto == null) return null;

        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());
        return room;
    }
}
