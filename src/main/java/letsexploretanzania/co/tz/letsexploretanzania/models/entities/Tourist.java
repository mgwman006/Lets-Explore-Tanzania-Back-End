package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tourists")
public class Tourist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @OneToMany(mappedBy = "tourist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TourBooking> bookings = new HashSet<>();
    @OneToOne( cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Tourist() {
    }

    public Tourist(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    public User getUser() {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
        if (user.getTourist() != this)
        {
            user.setTourist(this);
        }
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
