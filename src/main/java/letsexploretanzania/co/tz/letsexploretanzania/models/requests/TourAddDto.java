package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourDate;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourDateDTO;

import java.math.BigDecimal;

public record TourAddDto(
        @NotBlank(message = "Title is required")
        @Column(length = 100)
        String title,

        @NotBlank(message = "Description is required")
        @Column(length = 2000)
        String description,

        @NotNull(message = "pricePerPerson is required")
        @Positive(message = "Price must be a positive number")
        BigDecimal pricePerPerson,

        @Positive(message = "Duration must be a positive number")
        @NotNull(message = "Duration is required")
        int durationDays,

        @NotBlank(message = "Destination is required")
        String destination,

        @NotNull (message = "isAvailableAllTheTime is required")
        Boolean hasSpecificDates,

        TourDateDTO tourDates


) {
}
