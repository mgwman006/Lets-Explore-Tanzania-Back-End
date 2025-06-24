package letsexploretanzania.co.tz.letsexploretanzania.models.responses;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tour;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourActivity;

import java.util.ArrayList;
import java.util.List;

public record TourGuideDTO(
        Long id,
        MeetingPoint pickUpInformation,
        MeetingPoint endOfTourInformation,
        List<TourActivityDetailsDTO> tourActivities
) {
}
