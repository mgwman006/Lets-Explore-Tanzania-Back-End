package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDate;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourDateDTO;

import java.math.BigDecimal;
import java.util.List;

public record TourAddDto(
        @NotBlank(message = "Title is required")
        @Column(length = 100)
        String title,

        @NotBlank(message = "Description is required")
        @Column(length = 500)
        String overView,

        @Positive(message = "Duration must be a positive number")
        @NotNull(message = "Duration is required")
        int durationDays,

        @NotNull(message = "Destinations is required")
        List<String> destinations,

        @NotNull (message = "isAvailableAllTheTime is required")
        Boolean hasSpecificDates,

        TourDateDTO tourDates


) {
}
