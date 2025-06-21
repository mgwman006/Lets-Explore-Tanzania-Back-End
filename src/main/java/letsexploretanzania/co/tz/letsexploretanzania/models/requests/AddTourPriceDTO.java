package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import java.math.BigDecimal;

public record AddTourPriceDTO(
        int quantity,
        BigDecimal pricePerPerson,
        String currency
) {
}
