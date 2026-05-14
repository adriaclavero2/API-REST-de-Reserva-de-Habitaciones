package com.itacademy.api_rest_room_reservation.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
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

    @Positive
    @Column(nullable = false)
    private Integer capacity;

    @Positive
    @Column(nullable = false)
    private BigDecimal pricePerNight;
}
