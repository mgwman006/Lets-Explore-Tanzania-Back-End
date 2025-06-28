package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record OtpRequestDTO(
        @NotBlank
        String email
) {
}
