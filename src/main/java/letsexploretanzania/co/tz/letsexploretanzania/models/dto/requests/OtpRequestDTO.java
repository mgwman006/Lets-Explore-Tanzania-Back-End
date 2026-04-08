package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record OtpRequestDTO(
        @NotBlank
        String email
) {
}
