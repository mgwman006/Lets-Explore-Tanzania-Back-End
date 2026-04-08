package letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingAddDTO(
        @NotNull
        @Positive(message = "tour id must be greater than zero")
        Long tourId,
        @NotNull(message = "price can not be null")
        @Positive(message = "price must be positive")
        BigDecimal pricePerPerson,
        @Positive(message = "number of people must be positive")
        int numberOfPeople,
        BigDecimal totalPrice,
        LocalDate tourDate,
        @Size(max = 500, message = "maximum length of allowed email is 500 characters")
        String specialRequests,
        @NotNull(message = "operator ff is required")
        @Positive
        Long operatorId,
        @NotNull(message = "contact person is required")
        BookingContactPerson contactPerson
) {
}
