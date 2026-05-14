package responseDTOS;

import com.itacademy.api_rest_room_reservation.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {

    private Long id;

    private Long userId;

    private Long roomId;

    private LocalDate startDate;

    private LocalDate endDate;

    private ReservationStatus status;

    private LocalDateTime createdAt;
}
