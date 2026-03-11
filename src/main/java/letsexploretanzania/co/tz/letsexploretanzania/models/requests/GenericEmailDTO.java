package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.validation.constraints.NotBlank;

public record GenericEmailDTO(
  @NotBlank
  String bodyText,
  @NotBlank
  String toAddress,
  @NotBlank
  String subject
)
{
}
