package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserType;

public record UserDTO(
        Long id,
        String email,
        String password,
        String userType
) {
}
