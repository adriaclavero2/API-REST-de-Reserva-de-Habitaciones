package dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomResponseDTO {

    private Long id;

    private Integer roomNumber;

    private Integer capacity;

    private BigDecimal pricePerNight;
}
