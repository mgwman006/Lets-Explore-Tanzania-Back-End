package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.BookingStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.RoleNameEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.BookingUtils;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.constants.Constants;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.BookingAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.BookingContactPerson;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.booking.BookingCreatedDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.booking.BookingDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourBookingService {
    private final TourBookingRepository tourBookingRepository;
    private final TourRepository tourRepository;
    private final TourOperatorRepository tourOperatorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public TourBookingService(TourBookingRepository tourBookingRepository, TourRepository tourRepository, TourOperatorRepository tourOperatorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService)
    {
      this.tourBookingRepository = tourBookingRepository;
      this.tourRepository = tourRepository;
      this.tourOperatorRepository = tourOperatorRepository;
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailService = emailService;
    }

    public Result<BookingCreatedDTO> addBooking(BookingAddDTO bookingRequest)
    {
        try
        {
            Optional<Tour> optionalTour = tourRepository.findById(bookingRequest.tourId());
            if (optionalTour.isEmpty())
            {
                return Result.failure("Tour with id "+bookingRequest.tourId()+" do not exist");
            }
            Tour tour = optionalTour.get();

            Optional<TourOperator> optionalTourOperator = tourOperatorRepository.findById(bookingRequest.operatorId());
            if (optionalTourOperator.isEmpty())
            {
                return Result.failure("Operator with id "+bookingRequest.operatorId()+" not found");
            }

            Tourist tourist;
            Optional<User> optionalUser = userRepository.findByUserName(bookingRequest.contactPerson().email());
            if (optionalUser.isEmpty())
            {
                User user = new User(bookingRequest.contactPerson().email(), passwordEncoder.encode(UUID.randomUUID().toString()));
                user.addRole(new Role(RoleNameEnum.TOURIST));
                tourist = createAndReturnTourist(user,bookingRequest);
            }
            else
            {
                User user = optionalUser.get();
                tourist = user.getTourist();

                if (tourist == null)
                {
                    tourist = createAndReturnTourist(user,bookingRequest);
                }
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


            booking = tourBookingRepository.save(booking);
            tourist = booking.getTourist();

            String textBody = Constants.BOOKING_MESSAGE.formatted(
              tourist.getFirstName(),
              booking.getReferenceNumber(),
              tour.getTitle(),
              booking.getTourDate(),
              booking.getNumberOfPeople(),
              booking.getPricePerPerson(),
              booking.getTotalPrice(),
              booking.getStatus()
            );

            String htmlBody = Constants.BOOKING_MESSAGE_BODY_HTML.formatted(
              tourist.getFirstName(),
              booking.getReferenceNumber(),
              tour.getTitle(),
              booking.getTourDate(),
              booking.getNumberOfPeople(),
              booking.getPricePerPerson(),
              booking.getTotalPrice(),
              booking.getStatus()
            );

            emailService.sendGenericEmail(bookingRequest.contactPerson().email(), Constants.BOOKING_EMAIL_SUBJECT,textBody,htmlBody);

            return Result.success(
              Constants.SUCCESS,
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
                  tourist.getUser().getUsername(),
                  tourist.getPhoneNumber()
                )
              ));

        }
        catch (Exception exception)
        {
            return Result.failure(exception.getMessage());
        }
    }

    public Tourist createAndReturnTourist(User user, BookingAddDTO bookingRequest)
    {
        Tourist tourist = new Tourist(
          bookingRequest.contactPerson().firstName(),
          bookingRequest.contactPerson().lastName(),
          bookingRequest.contactPerson().phoneNumber());

        user.setTourist(tourist);
        user = userRepository.save(user);
        return user.getTourist();
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
          Constants.SUCCESS,
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
