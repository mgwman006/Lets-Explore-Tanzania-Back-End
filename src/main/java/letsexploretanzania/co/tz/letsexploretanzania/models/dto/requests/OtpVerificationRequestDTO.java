package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record OtpVerificationRequestDTO(
        @NotBlank
        String otp,
        @NotBlank
        String email
) {
}
