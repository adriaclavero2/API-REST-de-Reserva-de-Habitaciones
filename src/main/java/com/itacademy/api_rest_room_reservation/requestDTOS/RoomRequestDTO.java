package com.itacademy.api_rest_room_reservation.requestDTOS;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomRequestDTO {

    @NotNull(message = "Room number is required")
    private Integer roomNumber;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;

    @NotNull(message = "Price per night is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal pricePerNight;
}
