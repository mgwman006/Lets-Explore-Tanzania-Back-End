package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.math.BigDecimal;
import java.util.List;

public record TourDetailsListItemDto(
        Long id,
        String title,
        String overView,
        int durationDays,
        String bannerImageUrl,
        boolean hasSpecificDates,
        TourDateDTO tourDates,
        List<String> destinations,
        List<TourPriceDTO> tourPrice
) {
}
