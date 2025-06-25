package letsexploretanzania.co.tz.letsexploretanzania.models.entities;


import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tour_type")
@Entity
@Table(name = "tours")
public abstract class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String overView;
    private int durationDays;
    private String bannerImageUrl;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourDestination> destinations = new ArrayList<>();
    @OneToMany(
            mappedBy = "tour",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Photo> photos = new ArrayList<>(); // Additional images

    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    TourGuide tourGuide;

    // @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    // private List<Booking> bookings;



    public Tour() {
    }

    public Tour(
            String title,
            String overView,
            int durationDays) {
        this.title = title;
        this.overView = overView;
        this.durationDays = durationDays;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhotos(List<Photo> photos) {
        this.photos.addAll(photos);
    }
    public void addSinglePhoto(Photo photo) {
        this.photos.add(photo);
    }



    public List<TourDestination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TourDestination> destinations) {
        this.destinations = destinations;
    }

    public void addDestination(TourDestination tourDestination)
    {
        this.destinations.add(tourDestination);
    }

    public TourGuide getGuide() {
        return tourGuide;
    }

    public void setGuide(TourGuide guide) {
        this.tourGuide = guide;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    public void addPhotos(Photo photo) {
        this.photos.add(photo);
    }
    public void upDate(
            String title,
            String overView,
            int durationDays) {
        this.title = title;
        this.overView = overView;
        this.durationDays = durationDays;
    }
}

