package letsexploretanzania.co.tz.letsexploretanzania.models.entities;

import jakarta.persistence.*;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
public class TourBooking {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    Long id;
    private String customerName;
    private String email;
    private String phoneNumber;
    private BigDecimal pricePerPerson;
    private int numberOfPeople;
    private BigDecimal totalPrice;
    private LocalDate tourDate;
    @Column(length = 500)
    private String specialRequests;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
    @ManyToOne(
            cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "tourist_id")
    Tourist tourist;

    private String referenceNumber;


    public TourBooking() {
    }

    public TourBooking(
            String customerName,
            String email,
            String phoneNumber,
            BigDecimal pricePerPerson,
            int numberOfPeople,
            BigDecimal totalPrice,
            LocalDate tourDate,
            String specialRequests,
            BookingStatus status,
            String referenceNumber
    )
    {
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pricePerPerson = pricePerPerson;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
        this.tourDate = tourDate;
        this.specialRequests = specialRequests;
        this.status = status;
        this.referenceNumber = referenceNumber;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(BigDecimal pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getTourDate() {
        return tourDate;
    }

    public void setTourDate(LocalDate tourDate) {
        this.tourDate = tourDate;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
