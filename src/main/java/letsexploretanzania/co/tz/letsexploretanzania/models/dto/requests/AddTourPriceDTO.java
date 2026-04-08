package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;

import java.math.BigDecimal;

public record AddTourPriceDTO(
        int quantity,
        BigDecimal pricePerPerson,
        String currency
) {
}
