package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.math.BigDecimal;

public record TourDetailsListItemDto(
        Long id,
        String title,
        String description,
        BigDecimal pricePerPerson,
        int durationDays,
        String bannerImageUrl,
        String destination,
        boolean hasSpecificDates,
        TourDateDTO tourDates
) {
}
