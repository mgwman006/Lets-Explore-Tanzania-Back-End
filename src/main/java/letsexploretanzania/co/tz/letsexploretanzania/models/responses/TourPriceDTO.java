package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import java.math.BigDecimal;

public record TourPriceDTO(
        Long id,
        int quantity,
        BigDecimal pricePerPerson,
        CurrencyDTO currency
) {
}
