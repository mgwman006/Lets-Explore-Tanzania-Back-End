package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guides")
public class TourGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "location", column = @Column(name = "pickup_location")),
                    @AttributeOverride(name = "details", column = @Column(name = "pickup_details")),
                    @AttributeOverride(name = "dateTime", column = @Column(name ="pickup_dateTime"))
            }
    )
    private MeetingPoint pickUpInformation;
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "location", column = @Column(name = "end_location")),
                    @AttributeOverride(name = "details", column = @Column(name = "end_details")),
                    @AttributeOverride(name = "dateTime", column = @Column(name ="end_dateTime"))
            }
    )
    private MeetingPoint endOfTourInformation;

    @OneToMany(mappedBy = "tourGuide", cascade = CascadeType.ALL)
    List<TourActivity> tourActivities = new ArrayList<>();

    public TourGuide() {
    }

    public TourGuide(MeetingPoint pickUpInformation) {
        this.pickUpInformation = pickUpInformation;
    }

    public Long getId() {
        return id;
    }


    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public MeetingPoint getPickUpInformation() {
        return pickUpInformation;
    }

    public void setPickUpInformation(MeetingPoint pickUpInformation) {
        this.pickUpInformation = pickUpInformation;
    }

    public MeetingPoint getEndOfTourInformation() {
        return endOfTourInformation;
    }

    public void setEndOfTourInformation(MeetingPoint endOfTourInformation) {
        this.endOfTourInformation = endOfTourInformation;
    }

    public List<TourActivity> getTourActivities() {
        return tourActivities;
    }

    public void setTourActivities(List<TourActivity> tourActivities) {
        this.tourActivities = tourActivities;
    }

    public void addTourActivity(TourActivity tourActivity) {
        tourActivities.add(tourActivity);
    }
}
