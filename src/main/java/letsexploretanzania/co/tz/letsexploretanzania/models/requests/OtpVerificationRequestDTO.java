package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record OtpVerificationRequestDTO(
        @NotBlank
        String otp,
        @NotBlank
        String email
) {
}
