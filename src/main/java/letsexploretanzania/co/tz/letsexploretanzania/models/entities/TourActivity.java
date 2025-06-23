package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour_activities")
public class TourActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayNumber;
    private String title;
    @Column(length = 1000)
    private String description;
    private String location;
    private LocalTime startTime;
    private LocalTime endTime;
    @OneToMany(mappedBy = "tourActivity", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "tour_guide_id")
    private TourGuide tourGuide;

    // Constructors
    public TourActivity() {}

    public TourActivity(
            int dayNumber,
            String title,
            String description,
            String location,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.dayNumber = dayNumber;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo)
    {
        this.photos.add(photo);
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }

    public void setTourGuide(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }
}
