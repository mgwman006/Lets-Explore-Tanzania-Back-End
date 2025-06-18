package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String photoUrl;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

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

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
