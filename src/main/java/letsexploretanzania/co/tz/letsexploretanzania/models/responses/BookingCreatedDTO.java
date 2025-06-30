package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingCreatedDTO(
        Long id,
        Long touristId,
        String customerName,
        String email,
        String phoneNumber,
        BigDecimal pricePerPerson,
        int numberOfPeople,
        BigDecimal totalPrice,
        LocalDate tourDate,
        String specialRequests,
        BookingStatus status,
        String referenceNumber

) {
}
