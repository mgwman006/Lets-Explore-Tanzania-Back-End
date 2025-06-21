package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

public record TourCreatedDto(
        Long id,
        String title,
        String overView,
        int durationDays,
        boolean hasSpecificDates,
        TourDateDTO tourDates,
        List<String> destinations

) {
}
