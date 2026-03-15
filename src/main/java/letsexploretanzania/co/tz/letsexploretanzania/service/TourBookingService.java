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
import letsexploretanzania.co.tz.letsexploretanzania.repository.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TourBookingService {
    private final TourBookingRepository tourBookingRepository;
    private final TourRepository tourRepository;
    private final TouristRepository touristRepository;
    private final TourOperatorRepository tourOperatorRepository;
    private final UserRepository userRepository;

    public TourBookingService(
      TourBookingRepository tourBookingRepository,
      TourRepository tourRepository,
      TouristRepository touristRepository, TourOperatorRepository tourOperatorRepository, UserRepository userRepository) {
        this.tourBookingRepository = tourBookingRepository;
        this.tourRepository = tourRepository;
        this.touristRepository = touristRepository;
        this.tourOperatorRepository = tourOperatorRepository;
        this.userRepository = userRepository;
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

        Tourist tourist;
        if (!userRepository.existsByEmail(bookingRequest.contactPerson().email()))
        {
            User user = new User(
              bookingRequest.contactPerson().email(),
              bookingRequest.contactPerson().firstName(),
              UserType.TOURIST
            );

            tourist = new Tourist(
                    bookingRequest.contactPerson().firstName(),
                    bookingRequest.contactPerson().lastName(),
                    bookingRequest.contactPerson().phoneNumber());

            tourist.setUser(user);

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
            Optional<User> optionalUser = userRepository.findByEmail(bookingRequest.contactPerson().email());
            if (optionalUser.isEmpty())
                return Result.failure("User not found");

            User user = optionalUser.get();
            if (user.getUserType() != UserType.TOURIST)
                return Result.failure("User of type "+user.getUserType()+" not supported");

            tourist = user.getTourist();
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
                          tourist.getUser().getEmail(),
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
    public Result<String> updateTourPaymentStatus(String tourRefenceNumber, BookingStatus bookingStatus)
    {
        Optional<TourBooking> optionalTourBooking = tourBookingRepository.findByReferenceNumber(tourRefenceNumber);

        if (optionalTourBooking.isEmpty())
        {
            return  Result.failure("Booking with referenceNumber " + tourRefenceNumber + " not exist");
        }

        TourBooking tourBooking = optionalTourBooking.get();
        tourBooking.setStatus(bookingStatus);

        try
        {
            tourBookingRepository.save(tourBooking);
            return Result.success("success","success");
        }
        catch (Exception exception)
        {
            return Result.failure(exception.getMessage());
        }


    }
}
