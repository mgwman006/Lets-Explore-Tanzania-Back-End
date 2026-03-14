package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookingContactPerson(
  @NotBlank(message = "first name can not be blank")
  @Size(max = 50, message = "Maximum Size for name is 50 characters")
  String firstName,
  @NotBlank(message = "last name can not be blank")
  @Size(max = 50, message = "Maximum Size for name is 50 characters")
  String lastName,
  @NotBlank(message = "email can not be empty")
  @Size(max = 50, message = "maximum length of allowed email is 50 characters")
  @Email(message = "must be valid email")
  String email,
  @NotBlank(message = "phoneNumber can npt be blank")
  @Size(max = 20, message = "maximum number of allowed characters is 20")
  String phoneNumber
) {
}
