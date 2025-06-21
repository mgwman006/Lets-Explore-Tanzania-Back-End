package letsexploretanzania.co.tz.letsexploretanzania.models.entities;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String overView;
    private int durationDays;
    private String bannerImageUrl;
    private boolean hasSpecificDates;
    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    private TourDate tourDates;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourDestination> destinations = new ArrayList<>();
    @OneToMany(
            mappedBy = "tour",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Photo> photos = new ArrayList<>(); // Additional images

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    List<TourPrice> tourPrices = new ArrayList<>();

    // @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    // private List<Booking> bookings;


    public Tour() {
    }

    public Tour(
            String title,
            String overView,
            int durationDays,
            boolean hasSpecificDates) {
        this.title = title;
        this.overView = overView;
        this.durationDays = durationDays;
        this.hasSpecificDates = hasSpecificDates;
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

    public TourDate getTourDates() {
        return tourDates;
    }

    public void setTourDates(TourDate tourDates) {
        this.tourDates = tourDates;
    }

    public boolean isHasSpecificDates() {
        return hasSpecificDates;
    }

    public void setHasSpecificDates(boolean hasSpecificDates) {
        this.hasSpecificDates = hasSpecificDates;
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

    public List<TourPrice> getTourPrices() {
        return tourPrices;
    }

    public void setTourPrices(List<TourPrice> tourPrices) {
        this.tourPrices = tourPrices;
    }

    public void addTourPrice(TourPrice tourPrice)
    {
        this.tourPrices.add(tourPrice);
    }
}

