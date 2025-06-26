package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String photoUrl;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private TourActivity tourActivity;

    public Photo() {
    }

    public Photo(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getId() {
        return id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public TourActivity getTourActivity() {
        return tourActivity;
    }

    public void setTourActivity(TourActivity tourActivity) {
        this.tourActivity = tourActivity;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
