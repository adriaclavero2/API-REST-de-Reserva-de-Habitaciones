package com.itacademy.api_rest_room_reservation.repositories;

import com.itacademy.api_rest_room_reservation.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomNumber(Integer roomNumber);
}
