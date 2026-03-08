package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourType;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserType;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour.PrivateTourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.booking.BookingListItemDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.CreatedOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourListItemDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourDestinationRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourOperatorRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourRepository;
import letsexploretanzania.co.tz.letsexploretanzania.service.common.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TourOperatorService {

    private final TourOperatorRepository tourOperatorRepository;
    private final TourRepository tourRepository;
    private final AWSService awsService;
    private final TourDestinationRepository tourDestinationRepository;

    @Autowired
    public TourOperatorService(
            TourOperatorRepository tourOperatorRepository,
            TourRepository tourRepository,
            AWSService awsService,
            TourDestinationRepository tourDestinationRepository
    )
    {
        this.tourOperatorRepository = tourOperatorRepository;
        this.tourRepository = tourRepository;
        this.awsService = awsService;
        this.tourDestinationRepository = tourDestinationRepository;
    }

    public Result<CreatedOperatorDto> registerOperator(AddOperatorDto operatorDto)
    {
        User user = new User(
                operatorDto.email(),
                operatorDto.passWord(),
                UserType.TOUROPERATOR
        );

        TourOperator tourOperator = new TourOperator(
                operatorDto.firstName(),
                operatorDto.lastName(),
                operatorDto.email(),
                operatorDto.phone()
        );

        tourOperator.setUser(user);
        user.setTourOperator(tourOperator);

        try
        {
            tourOperator = tourOperatorRepository.save(tourOperator);
            return Result.success(
                    "success",
                    new CreatedOperatorDto(
                            tourOperator.getId(),
                            tourOperator.getFirstName(),
                            tourOperator.getLastName(),
                            tourOperator.getEmail(),
                            tourOperator.getPhone()
                    )
            );
        }
        catch (Exception e)
        {
            return  Result.failure(e.getMessage());
        }
    }

    public Result<List<TourListItemDTO>> getTours(Long operatorId) {
        Optional<TourOperator> tourOperator = tourOperatorRepository.findById(operatorId);
        if (tourOperator.isEmpty()) {
            return Result.failure(
                    "Operator of id " + operatorId + " not found!"
            );
        }

        TourOperator operator = tourOperator.get();
        Set<Tour> tours = operator.getTours();

        return Result.success(
                "success",
                tours
                .stream()
                .map(
                        tour ->
                                new TourListItemDTO(
                                        tour.getId(),
                                        tour.getTitle(),
                                        tour.getOverView(),
                                        tour.getDurationDays(),
                                        tour.getBannerImageUrl(),
                                        tour instanceof PrivateTour ? TourType.PRIVATE.name() : TourType.PUBLIC.name(),
                                        tour.isLive()
                                )
                ).toList()
        );

    }

    public Result<PrivateTourCreatedDto> addPrivateTour(
            Long operatorId,
            PrivateTourAddDto tourRequest,
            MultipartFile photo) throws IOException
    {
        Optional<TourOperator> optionalTourOperator = tourOperatorRepository.findById(operatorId);
        if (optionalTourOperator.isEmpty()) {
            return Result.failure("Operator of id " + operatorId + " not found!");
        }

        TourOperator tourOperator = optionalTourOperator.get();

        PrivateTour tour = new PrivateTour(
                tourRequest.title(),
                tourRequest.overView(),
                tourRequest.durationDays()
        );

        //Add a Guide
        TourGuide tourGuide = new TourGuide();
        tourGuide.setTour(tour);
        tour.setGuide(tourGuide);


        //Add Destinations
        for(String destination : tourRequest.destinations())
        {
            TourDestinationEnum tourDestinationEnum = TourDestinationEnum.valueOf(destination.toUpperCase());
            Optional<TourDestination> optionalTourDestination = tourDestinationRepository.findByName(tourDestinationEnum);

            if (optionalTourDestination.isPresent())
            {
                tour.addDestination(optionalTourDestination.get());
            }

        }

        //Add Banner Image
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        String formattedNow = String.valueOf(now.getYear())+
                String.valueOf(now.getMonthValue())+
                String.valueOf(now.getDayOfMonth())+
                String.valueOf(now.getHour())+
                String.valueOf(now.getMinute())+
                String.valueOf(now.getSecond())+
                String.valueOf(now.getNano());

        String objectName = formattedNow+"banners/"+photo.getOriginalFilename();

        String key = awsService.saveImageToS3(photo, objectName);
        tour.setBannerImageUrl(key);

        //Set Operator
        tourOperator.addTour(tour);
        tour.setOperator(tourOperator);

        try {
            tour = tourRepository.save(tour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        return Result.success(
                "tour successfully created",
                new PrivateTourCreatedDto(
                        tour.getId(),
                        tour.getGuide().getId(),
                        tour.getTitle(),
                        tour.getOverView(),
                        tour.getDurationDays(),
                        tour.getBannerImageUrl(),
                        tour.getDestinations()
                                .stream()
                                .map(d-> d.getName().getName()).toList()
                )
        );
    }

    public Result<List<BookingListItemDTO>> getBookings(Long operatorId)
    {
        Optional<TourOperator> tourOperator = tourOperatorRepository.findById(operatorId);
        if (tourOperator.isEmpty()) {
            return Result.failure("Operator of id " + operatorId + " not found!");
        }

        TourOperator operator = tourOperator.get();
        Set<TourBooking> bookings = operator.getBookings();

        return Result.success(
          "success",
          bookings
            .stream()
            .map(
              booking ->
                new BookingListItemDTO(
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
                )
            ).toList()
        );
    }
}
