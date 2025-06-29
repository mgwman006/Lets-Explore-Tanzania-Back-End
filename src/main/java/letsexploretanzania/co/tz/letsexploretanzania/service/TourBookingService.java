package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.BookingUtils;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tour;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourBooking;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tourist;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.BookingAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.BookingCreatedDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourBookingRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class TourBookingService {
    private final TourBookingRepository tourBookingRepository;
    private final TourRepository tourRepository;
    private final TouristRepository touristRepository;

    public TourBookingService(
            TourBookingRepository tourBookingRepository,
            TourRepository tourRepository,
            TouristRepository touristRepository) {
        this.tourBookingRepository = tourBookingRepository;
        this.tourRepository = tourRepository;
        this.touristRepository = touristRepository;
    }

    public Result<BookingCreatedDTO> addBooking(BookingAddDTO bookingRequest)
    {
        Optional<Tour> optionalTour = tourRepository.findById(bookingRequest.tourId());
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+bookingRequest.tourId()+" not exist");
        Tour tour = optionalTour.get();

        Optional<Tourist> optionalTourist = touristRepository.findByEmail(bookingRequest.email());
        Tourist tourist = new Tourist();
        if (optionalTourist.isEmpty())
        {
            tourist = new Tourist(
                    bookingRequest.customerName(),
                    bookingRequest.email(),
                    bookingRequest.phoneNumber());
            try {
                tourist = touristRepository.save(tourist);
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        }
        else
        {
            tourist = optionalTourist.get();
        }

        String bookingRefenceNumber = BookingUtils.generateBookingReference();

        TourBooking booking = new TourBooking(
                bookingRequest.customerName(),
                bookingRequest.email(),
                bookingRequest.phoneNumber(),
                bookingRequest.pricePerPerson(),
                bookingRequest.numberOfPeople(),
                bookingRequest.totalPrice(),
                bookingRequest.tourDate(),
                bookingRequest.specialRequests(),
                BookingStatus.NEW,
                bookingRefenceNumber
        );

        booking.setTourist(tourist);
        tourist.addBooking(booking);

        booking.setTour(tour);
        tour.addBooking(booking);

        try {
            booking = tourBookingRepository.save(booking);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }


        return Result.success(
                "success",
                new BookingCreatedDTO(
                        booking.getId(),
                        booking.getTourist().getId(),
                        booking.getCustomerName(),
                        booking.getEmail(),
                        booking.getPhoneNumber(),
                        booking.getPricePerPerson(),
                        booking.getNumberOfPeople(),
                        booking.getTotalPrice(),
                        booking.getTourDate(),
                        booking.getSpecialRequests(),
                        booking.getStatus(),
                        booking.getReferenceNumber()
                ));


    }
}
