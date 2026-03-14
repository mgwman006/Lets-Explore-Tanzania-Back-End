package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserType;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.BookingUtils;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.BookingAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.BookingContactPerson;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.booking.BookingCreatedDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.booking.BookingDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourBookingRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourOperatorRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TouristRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TourBookingService {
    private final TourBookingRepository tourBookingRepository;
    private final TourRepository tourRepository;
    private final TouristRepository touristRepository;
    private final TourOperatorRepository tourOperatorRepository;

    public TourBookingService(
      TourBookingRepository tourBookingRepository,
      TourRepository tourRepository,
      TouristRepository touristRepository, TourOperatorRepository tourOperatorRepository) {
        this.tourBookingRepository = tourBookingRepository;
        this.tourRepository = tourRepository;
        this.touristRepository = touristRepository;
      this.tourOperatorRepository = tourOperatorRepository;
    }

    public Result<BookingCreatedDTO> addBooking(BookingAddDTO bookingRequest)
    {
        Optional<Tour> optionalTour = tourRepository.findById(bookingRequest.tourId());
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+bookingRequest.tourId()+" not exist");
        Tour tour = optionalTour.get();

        Optional<TourOperator> optionalTourOperator = tourOperatorRepository.findById(bookingRequest.operatorId());
        if (optionalTourOperator.isEmpty())
        {
            return Result.failure("Operator with id "+bookingRequest.operatorId()+" not found");
        }

        Optional<Tourist> optionalTourist = touristRepository.findByEmail(bookingRequest.contactPerson().email());
        Tourist tourist;
        if (optionalTourist.isEmpty())
        {
            User user = new User(
              bookingRequest.contactPerson().email(),
              bookingRequest.contactPerson().firstName(),
              UserType.TOURIST
            );

            tourist = new Tourist(
                    bookingRequest.contactPerson().firstName(),
                    bookingRequest.contactPerson().lastName(),
                    bookingRequest.contactPerson().email(),
                    bookingRequest.contactPerson().phoneNumber());

            tourist.setUser(user);
            user.setTourist(tourist);

            try
            {
                tourist = touristRepository.save(tourist);
            }
            catch (Exception e)
            {
                return Result.failure(e.getMessage());
            }
        }
        else
        {
            tourist = optionalTourist.get();
        }

        String bookingRefenceNumber = BookingUtils.generateBookingReference();

        TourBooking booking = new TourBooking(
                bookingRequest.pricePerPerson(),
                bookingRequest.numberOfPeople(),
                bookingRequest.totalPrice(),
                bookingRequest.tourDate(),
                bookingRequest.specialRequests(),
                BookingStatus.PENDING_PAYMENT,
                bookingRefenceNumber
        );

        booking.setTourist(tourist);
        tourist.addBooking(booking);

        TourOperator tourOperator = optionalTourOperator.get();
        booking.setOperator(tourOperator);
        tourOperator.addBooking(booking);

        booking.setTour(tour);
        tour.addBooking(booking);

        try {
            booking = tourBookingRepository.save(booking);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        tourist = booking.getTourist();
        return Result.success(
                "success",
                new BookingCreatedDTO(
                        booking.getId(),
                        booking.getTourist().getId(),
                        booking.getPricePerPerson(),
                        booking.getNumberOfPeople(),
                        booking.getTotalPrice(),
                        booking.getTourDate(),
                        booking.getSpecialRequests(),
                        booking.getStatus(),
                        booking.getReferenceNumber(),
                        new BookingContactPerson(
                          tourist.getFirstName(),
                          tourist.getLastName(),
                          tourist.getEmail(),
                          tourist.getPhoneNumber()
                        )
                ));


    }

    public Result<BookingDetailsDTO> getTourById(Long bookingId)
    {
        Optional<TourBooking> optionalTourBooking = tourBookingRepository.findById(bookingId);

        if (optionalTourBooking.isEmpty())
        {
            return  Result.failure("Booking with id " + bookingId + " not exist");
        }
        TourBooking tourBooking = optionalTourBooking.get();
        return Result.success(
          "success",
          new BookingDetailsDTO
            (
              tourBooking.getId(),
              tourBooking.getTourist().getId(),
              tourBooking.getCustomerName(),
              tourBooking.getEmail(),
              tourBooking.getPhoneNumber(),
              tourBooking.getPricePerPerson(),
              tourBooking.getNumberOfPeople(),
              tourBooking.getTotalPrice(),
              tourBooking.getTourDate(),
              tourBooking.getSpecialRequests(),
              tourBooking.getStatus(),
              tourBooking.getReferenceNumber()
            )
        );
    }
}
