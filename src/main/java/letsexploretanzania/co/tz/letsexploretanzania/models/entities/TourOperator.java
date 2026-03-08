package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "operators")
public class TourOperator {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "operator", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Tour> tours = new HashSet<>();
    @OneToMany(mappedBy = "operator", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    private Set<TourBooking> bookings = new HashSet<>();

    public TourOperator() {
    }

    public TourOperator(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Tour> getTours() {
        return tours;
    }

    public void addTour(Tour tour) {
        this.tours.add(tour);
    }
    public void addBooking(TourBooking  tourBooking)
    {
        this.bookings.add(tourBooking);
    }

    public Set<TourBooking> getBookings()
    {
        return bookings;
    }

    @Override
    public String toString() {
        return "TourOperator{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", user=" + user +
                '}';
    }
}
