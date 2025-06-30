package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailVerificationRequestDTO(
        @NotBlank(message = "Email can not be blank")
        @Size(max = 50, message = "maximum length of allowed email is 50 characters")
        String email
) {
}
