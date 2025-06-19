package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TourDateDTO(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
