package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDate;

import java.math.BigDecimal;
import java.util.List;

public record TourDetailsDto(
        Long id,
        String title,
        String description,
        BigDecimal pricePerPerson,
        int durationDays,
        String bannerImageUrl,
        String destination,
        List<String> photos,
        boolean hasSpecificDates,
        TourDateDTO tourDates
) {
}
