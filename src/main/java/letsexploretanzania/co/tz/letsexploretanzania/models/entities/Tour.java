package letsexploretanzania.co.tz.letsexploretanzania.models.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tour_type")
@Entity
@Table(name = "tours")
public abstract class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 500)
    @Size(max = 500, message = "Overview must be 500 characters or less")
    private String overView;
    private int durationDays;
    private String bannerImageUrl;

    private boolean isLive;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "tour_destination",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id")
    )
    private Set<TourDestination> destinations = new HashSet<>();

    @OneToMany(
            mappedBy = "tour",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Photo> photos = new ArrayList<>(); // Additional images

    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    TourGuide tourGuide;

     @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private Set<TourBooking> bookings = new HashSet<>();



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

    public Set<TourDestination> getDestinations() {
        return destinations;
    }

    public void setDestinations(Set<TourDestination> destinations) {
        this.destinations = destinations;
    }
    public void addDestinations(TourDestination destination) {
        this.destinations.add(destination);
    }

    public void removeAllDestinations() {
        this.destinations.clear();
    }

    public Set<TourBooking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<TourBooking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(TourBooking booking)
    {
        this.bookings.add(booking);
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}

