package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record PassWordResetDTO(
        @NotBlank(message = "email can not be empty/null")
        String email,
        @NotBlank(message = "password can not be empty/null")
        String passWord
) {
}
