package letsexploretanzania.co.tz.letsexploretanzania.common.dtos;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record MeetingPoint(
        String location,
        String details,
        LocalDateTime dateTime
) {
}
