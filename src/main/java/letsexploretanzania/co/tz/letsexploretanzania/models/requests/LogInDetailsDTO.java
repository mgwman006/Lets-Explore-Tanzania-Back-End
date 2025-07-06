package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record LogInDetailsDTO(
        @NotBlank(message = "Email can not be null or empty")
        String email,
        @NotBlank(message = "PassWord can not be null or empty")
        String passWord
) {
}
