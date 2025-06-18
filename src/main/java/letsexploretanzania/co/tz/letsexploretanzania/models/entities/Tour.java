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
    private String description;
    private BigDecimal pricePerPerson;
    private int durationDays;
    private String bannerImageUrl; // Main image for display
    @OneToMany(
            mappedBy = "tour",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Photo> photos = new ArrayList<>(); // Additional images
    private boolean isAvailableAllTheTime;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourDate> availableDates = new ArrayList<>();
    private String destination;

    // @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    // private List<Booking> bookings;


    public Tour() {
    }

    public Tour(String title, String description, BigDecimal pricePerPerson, int durationDays,String destination) {
        this.title = title;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.durationDays = durationDays;
        this.destination = destination;
    }

    public Tour(String title, String description, BigDecimal pricePerPerson, int durationDays, boolean isAvailableAllTheTime, String destination) {
        this.title = title;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.durationDays = durationDays;
        this.isAvailableAllTheTime = isAvailableAllTheTime;
        this.destination = destination;
    }

    public Tour(String title, String description, BigDecimal pricePerPerson, int durationDays, boolean isAvailableAllTheTime,String destination, List<TourDate> availableDates) {
        this.title = title;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.durationDays = durationDays;
        this.isAvailableAllTheTime = isAvailableAllTheTime;
        this.destination = destination;
        this.availableDates = availableDates;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
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

    public boolean isAvailableAllTheTime() {
        return isAvailableAllTheTime;
    }

    public void setAvailableAllTheTime(boolean availableAllTheTime) {
        isAvailableAllTheTime = availableAllTheTime;
    }

    public List<TourDate> getAvailableDates() {
        return availableDates;
    }

    public void addAvailableDates(List<TourDate> availableDates) {
        this.availableDates.addAll(availableDates);
    }
    public void addSingleAvailableDate(TourDate availableDate) {
        this.availableDates.add(availableDate);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

