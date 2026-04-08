package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

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
