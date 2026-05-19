package com.itacademy.api_rest_room_reservation.repositories;

import com.itacademy.api_rest_room_reservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.room.id = :roomId " +
            "AND r.status = 'ACTIVE' " +
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsRoomOverlap(@Param("roomId") Long roomId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
            "WHERE r.user.id = :userId " +
            "AND r.status = 'ACTIVE' " +
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsUserOverlap(@Param("userId") Long userId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);
}