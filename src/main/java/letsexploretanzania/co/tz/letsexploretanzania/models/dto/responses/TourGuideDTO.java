package letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses;

import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;

import java.util.List;

public record TourGuideDTO(
        Long id,
        MeetingPoint pickUpInformation,
        MeetingPoint endOfTourInformation,
        List<TourActivityDetailsDTO> tourActivities
) {
}
