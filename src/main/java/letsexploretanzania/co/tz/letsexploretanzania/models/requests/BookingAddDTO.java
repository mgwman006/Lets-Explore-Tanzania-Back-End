package letsexploretanzania.co.tz.letsexploretanzania.models.requests;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingAddDTO(
        @NotNull
        @Positive(message = "tour id must be greater than zero")
        Long tourId,
        @NotBlank(message = "name can not be blank")
        @Size(max = 50, message = "Maximum Size for name is 50 characters")
        String customerName,
        @NotBlank(message = "email can not be empty")
        @Size(max = 50, message = "maximum length of allowed email is 50 characters")
        @Email(message = "must be valid email")
        String email,
        @NotBlank(message = "phoneNumber can npt be blank")
        @Size(max = 20, message = "maximum number of allowed characters is 20")
        String phoneNumber,
        @NotNull(message = "price can not be null")
        @Positive(message = "price must be positive")
        BigDecimal pricePerPerson,
        @Positive(message = "number of people must be positive")
        int numberOfPeople,
        BigDecimal totalPrice,
        LocalDate tourDate,
        @NotBlank(message = "specialRequests can not be empty")
        @Size(max = 500, message = "maximum length of allowed email is 500 characters")
        String specialRequests
) {
}
