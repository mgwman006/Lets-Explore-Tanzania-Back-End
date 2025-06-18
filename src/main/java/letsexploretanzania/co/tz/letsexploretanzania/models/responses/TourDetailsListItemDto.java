package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.math.BigDecimal;

public record TourDetailsListItemDto(
        Long id,
        String title,
        String description,
        BigDecimal pricePerPerson,
        int duration,
        boolean isAvailableAllTheTime,
        String destination,
        String getBannerImageUrl
) {
}
