package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TourCreatedDto(
        Long id,
        String title,
        String description,
        BigDecimal pricePerPerson,
        int duration,
        boolean isAvailableAllTheTime,
        String destination
) {
}
