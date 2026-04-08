package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import java.time.LocalTime;

public record TourActivityAddDTO(
        int dayNumber,
        String title,
        String description,
        String location,
        LocalTime startTime,
        LocalTime endTime
) {
}
