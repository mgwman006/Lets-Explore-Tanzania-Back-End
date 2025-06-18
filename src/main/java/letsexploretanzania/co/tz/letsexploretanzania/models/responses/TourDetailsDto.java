package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDate;

import java.math.BigDecimal;
import java.util.List;

public record TourDetailsDto(
        Long id,
        String title,
        String description,
        BigDecimal pricePerPerson,
        int duration,
        boolean isAvailableAllTheTime,
        String destination,
        String getBannerImageUrl,
        List<String> photos,
        List<TourDateDTO> AvailableDates
) {
}
