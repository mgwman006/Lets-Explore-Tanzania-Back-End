package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record AddOperatorDto(
        @NotBlank(message = "FirstName can not be null or empty")
        String firstName,
        @NotBlank(message = "FirstName can not be null or empty")
        String lastName,
        @NotBlank(message = "LastName can not be null or empty")
        String email,
        @NotBlank(message = "Phone Number can not be null or empty")
        String phone,
        @NotBlank(message = "Password can not be null or empty")
        String passWord
) {
}
