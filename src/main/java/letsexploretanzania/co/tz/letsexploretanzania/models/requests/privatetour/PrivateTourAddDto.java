package letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record PrivateTourAddDto(
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
        List<String> destinations

) {
}
