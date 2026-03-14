package letsexploretanzania.co.tz.letsexploretanzania.models.responses.booking;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.BookingContactPerson;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingCreatedDTO(
        Long id,
        Long touristId,
        BigDecimal pricePerPerson,
        int numberOfPeople,
        BigDecimal totalPrice,
        LocalDate tourDate,
        String specialRequests,
        BookingStatus status,
        String referenceNumber,
        BookingContactPerson contactPerson

) {
}
