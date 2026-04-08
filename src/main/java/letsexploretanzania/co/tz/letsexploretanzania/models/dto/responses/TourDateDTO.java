package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TourDateDTO(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
