package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record PassWordResetDTO(
        @NotBlank(message = "email can not be empty/null")
        String email,
        @NotBlank(message = "password can not be empty/null")
        String passWord
) {
}
