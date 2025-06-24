package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

public class PublicTour {
    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    private TourDate tourDates;
}
