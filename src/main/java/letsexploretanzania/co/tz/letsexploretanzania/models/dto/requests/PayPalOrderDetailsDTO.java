package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record PayPalOrderDetailsDTO(
  @NotBlank(message = "Amount can not be null or empty")
  String amount,
  @NotBlank(message = "Currency can not be null or empty")
  String currency,
  @NotBlank(message = "referenceNumber can not be null or empty")
  String referenceNumber
) { }
