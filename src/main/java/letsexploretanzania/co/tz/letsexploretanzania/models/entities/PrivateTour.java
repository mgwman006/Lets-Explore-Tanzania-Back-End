package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourType;

import java.util.ArrayList;
import java.util.List;



@Entity
@DiscriminatorValue("PRIVATE")
public class PrivateTour extends Tour{

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TourPrice> tourPrices = new ArrayList<>();

    public PrivateTour() {
        super(
                "",
                "",
                0
        );
    }

    public PrivateTour(
            String title,
            String overView,
            int durationDays)
    {
        super(title,overView,durationDays);
    }

    public List<TourPrice> getTourPrices() {
        return tourPrices;
    }

    public void setTourPrices(List<TourPrice> tourPrices) {
        this.tourPrices = tourPrices;
    }
    public void addTourPrice(TourPrice tourPrice) {
        this.tourPrices.add(tourPrice);
    }

}
