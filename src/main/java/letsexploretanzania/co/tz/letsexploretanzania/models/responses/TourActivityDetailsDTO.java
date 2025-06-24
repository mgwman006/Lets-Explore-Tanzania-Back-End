package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.time.LocalTime;
import java.util.List;

public record TourActivityDetailsDTO(
        Long id,
        int dayNumber,
        String title,
        String description,
        String location,
        LocalTime startTime,
        LocalTime endTime,
        List<String> photos
) {
}
