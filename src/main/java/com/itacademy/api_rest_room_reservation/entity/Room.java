package com.itacademy.api_rest_room_reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer roomNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private BigDecimal pricePerNight;
}
