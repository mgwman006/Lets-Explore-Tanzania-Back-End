package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.time.LocalDate;

public record TourDateDTO(
        LocalDate startDate,
        LocalDate endDate
) {
}
