package letsexploretanzania.co.tz.letsexploretanzania.service;

import jakarta.transaction.Transactional;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.DeleteResponseDto;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.CurrencyEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddTourPriceDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour.PrivateTourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour.PrivateTourUpdateDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsListItemDto;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourDestinationRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourRepository;
import letsexploretanzania.co.tz.letsexploretanzania.service.common.AWSService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PrivateTourService {

    private final TourRepository tourRepository;
    private final AWSService awsService;
    private final TourDestinationRepository tourDestinationRepository;

    public PrivateTourService(
            TourRepository tourRepository,
            AWSService awsService,
            TourDestinationRepository tourDestinationRepository)
    {
        this.tourRepository = tourRepository;
        this.awsService = awsService;
        this.tourDestinationRepository = tourDestinationRepository;
    }


    public Result<PrivateTourCreatedDto> addTour(PrivateTourAddDto tourRequest, MultipartFile bannerImage) throws IOException {
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

        String objectName = formattedNow+"banners/"+bannerImage.getOriginalFilename();

        String key = awsService.saveImageToS3(bannerImage, objectName);
        tour.setBannerImageUrl(key);

        try {
            tour = tourRepository.save(tour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        return Result.success("tour successfully created",
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

    public Result<List<PrivateTourDetailsListItemDto>> getAllTours()
    {
        List<Tour> tours;
        try {
            tours = tourRepository.findAll();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        List<PrivateTour> privateTours = tours
                .stream()
                .filter(tour -> tour instanceof PrivateTour)
                .map(tour -> (PrivateTour) tour)
                .toList();
        return Result
                .success(
                        "data fetch is successfully",
                        privateTours.stream()
                                .map(
                                        t -> new PrivateTourDetailsListItemDto(
                                                t.getId(),
                                                t.getTitle(),
                                                t.getOverView(),
                                                t.getDurationDays(),
                                                t.getBannerImageUrl(),
                                                t.getDestinations()
                                                        .stream()
                                                        .map(d->d.getName().getName()).toList(),
                                                t.getTourPrices()
                                                        .stream()
                                                        .map(
                                                        p-> new TourPriceDTO
                                                                (
                                                                    p.getId(),
                                                                    p.getQuantity(), p.getPricePerPerson(),
                                                                    new CurrencyDTO(
                                                                            p.getCurrency().getCode(),
                                                                            p.getCurrency().getSymbol()
                                                                    )
                                                                )
                                                        ).toList()
                                        )
                                ).toList()

                );
    }

    public Result<PrivateTourDetailsDto> getTourById(Long tourId) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);

        if (optionalTour.isEmpty())
        {
            return  Result.failure("Tour with id " + tourId + " not exist");
        }

        PrivateTour privateTour = (PrivateTour) optionalTour.get();

        List<String> photos = privateTour.getPhotos().stream().map(Photo::getPhotoUrl).toList();
        return Result.success(
                "success",
                new PrivateTourDetailsDto
                (
                    privateTour.getId(),
                    privateTour.getTitle(),
                    privateTour.getOverView(),
                    privateTour.getDurationDays(),
                    privateTour.getBannerImageUrl(),
                    privateTour.getDestinations()
                    .stream()
                    .map(d-> d.getName().getName()).toList(),
                    privateTour.getTourPrices()
                    .stream()
                    .map(p ->
                            new TourPriceDTO(
                                    p.getId(),
                                    p.getQuantity(),
                                    p.getPricePerPerson(),
                                    new CurrencyDTO(
                                            p.getCurrency().getCode(),
                                            p.getCurrency().getSymbol()
                                    )
                            )).toList(),
                    photos
                )
        );
    }

    public Result<DeleteResponseDto> deleteTourById(Long tourId) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id " + tourId + " not exist");
        try {
            tourRepository.delete(optionalTour.get());
        } catch (DataIntegrityViolationException | JpaSystemException e) {
            return Result.failure(e.getMessage());
        }

        return Result.success("success", new DeleteResponseDto("deleted successfully"));
    }

    public Result<String> addBannerImage(Long tourId, MultipartFile image) {
        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id " + tourId + " not exist");

        Tour tour = optionalTour.get();

        try {

            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
            String formattedNow = String.valueOf(now.getYear())+
                    String.valueOf(now.getMonthValue())+
                    String.valueOf(now.getDayOfMonth())+
                    String.valueOf(now.getHour())+
                    String.valueOf(now.getMinute())+
                    String.valueOf(now.getSecond())+
                    String.valueOf(now.getNano());

            String objectName = formattedNow+"banners/"+image.getOriginalFilename();

            String key = awsService.saveImageToS3(image,objectName);
            tour.setBannerImageUrl(key);
            tour = tourRepository.save(tour);

            return Result.success("Banner added successfully", tour.getBannerImageUrl());
        } catch (Exception e) {

            return Result.failure(e.getMessage());
        }

    }

    public Result<List<String>> addPhotos(Long tourId, List<MultipartFile> photos) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+tourId+" not exist");
        Tour tour = optionalTour.get();

        for (MultipartFile photo : photos)
        {
            try {
                LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
                String formattedNow = String.valueOf(now.getYear())+
                        String.valueOf(now.getMonthValue())+
                        String.valueOf(now.getDayOfMonth())+
                        String.valueOf(now.getHour())+
                        String.valueOf(now.getMinute())+
                        String.valueOf(now.getSecond())+
                        String.valueOf(now.getNano());

                String objectName = formattedNow+"tourImages/"+photo.getOriginalFilename();
                String imageUrl = awsService.saveImageToS3(photo, objectName);
                Photo newPhoto = new Photo(imageUrl);
                newPhoto.setTour(tour);
                tour.addSinglePhoto(newPhoto);
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        }
        tour = tourRepository.save(tour);
        List<String> images = tour.getPhotos().stream().map( p -> p.getPhotoUrl()).toList();
        return Result.success(
                "success",
                images
        );
    }

    public Result<List<TourPriceDTO>> addTourPrice(Long tourId, List<AddTourPriceDTO> tourPricesRequest)
    {
        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+ tourId+" do not exist");

        PrivateTour tour = (PrivateTour) optionalTour.get();
        for (AddTourPriceDTO tourPriceDTO : tourPricesRequest)
        {
            TourPrice tourPrice = new TourPrice(
                    tourPriceDTO.quantity(),
                    tourPriceDTO.pricePerPerson(),
                    CurrencyEnum.valueOf(tourPriceDTO.currency().toUpperCase())
            );

            tourPrice.setTour(tour);
            tour.addTourPrice(tourPrice);
        }

        try {
            tour = tourRepository.save(tour);
        }
        catch (DataIntegrityViolationException e) {
            return Result.failure("Data integrity error: " + e.getMessage());
        }  catch (Exception e) {
            return Result.failure("Unexpected error: " + e.getMessage());
        }

        List<TourPrice> tourPrices = tour.getTourPrices();
        return Result.success(
                "price added",
                tourPrices
                        .stream()
                        .map(p->
                                new TourPriceDTO(
                                        p.getId(),
                                        p.getQuantity(),
                                        p.getPricePerPerson(),
                                        new CurrencyDTO(
                                                p.getCurrency().getCode(),
                                                p.getCurrency().getSymbol()
                                        )

                                )
                        ).toList()
        );

    }

    public Result<TourGuideDTO> getTourGuide(Long tourId) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+tourId+" do not exist");

        TourGuide tourGuide = optionalTour.get().getGuide();
        List<TourActivity> tourActivities = tourGuide.getTourActivities();
        return Result.success(
                "success",
                new TourGuideDTO(
                        tourGuide.getId(),
                        tourGuide.getPickUpInformation(),
                        tourGuide.getEndOfTourInformation(),
                        tourActivities
                                .stream()
                                .map(
                                        a ->
                                        new TourActivityDetailsDTO(
                                                a.getId(),
                                                a.getDayNumber(),
                                                a.getTitle(),
                                                a.getDescription(),
                                                a.getLocation(),
                                                a.getStartTime(),
                                                a.getEndTime(),
                                                a.getPhotos()
                                                        .stream()
                                                        .map(p -> p.getPhotoUrl()).toList()
                                        )
                                ).toList()


                )
        );
    }

    @Transactional
    public Result<PrivateTourCreatedDto> updateTour(
            Long tourId,
            PrivateTourUpdateDto tourRequest)
    {
        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
        {
            return  Result.failure("Tour with id " + tourId + " not exist");
        }

        PrivateTour privateTour = (PrivateTour) optionalTour.get();

        privateTour.upDate(
                tourRequest.title(),
                tourRequest.overView(),
                tourRequest.durationDays()
        );


        //Clear Existing Destinations
        privateTour.removeAllDestinations();

        //Add Destinations
        for(String destination : tourRequest.destinations())
        {
            TourDestinationEnum tourDestinationEnum = TourDestinationEnum.valueOf(destination.toUpperCase());
            Optional<TourDestination> optionalTourDestination = tourDestinationRepository.findByName(tourDestinationEnum);

            if (optionalTourDestination.isPresent())
            {
                if (!privateTour.getDestinations().contains(optionalTourDestination.get())) {
                    privateTour.addDestination(optionalTourDestination.get());
                }
            }

        }
        


        try {
            privateTour = tourRepository.save(privateTour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        //Get a Tour Guid
        TourGuide tourGuide = privateTour.getGuide();
        if (tourGuide ==null)
        {
            tourGuide = new TourGuide();
            tourGuide.setTour(privateTour);
            privateTour.setGuide(tourGuide);
            try {
                privateTour = tourRepository.save(privateTour);
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        }

        tourGuide = privateTour.getGuide();
        return Result.success("tour successfully created",
                new PrivateTourCreatedDto(
                        privateTour.getId(),
                        tourGuide.getId(),
                        privateTour.getTitle(),
                        privateTour.getOverView(),
                        privateTour.getDurationDays(),
                        privateTour.getBannerImageUrl(),
                        privateTour.getDestinations()
                                .stream()
                                .map(d-> d.getName().getName()).toList()
                )
        );
    }

    @Transactional
    public Result<List<TourPriceDTO>> deleteTourPrice(Long tourId, Long priceId) {
        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+ tourId+" do not exist");

        PrivateTour tour = (PrivateTour) optionalTour.get();



        Optional<TourPrice> optionalTourPrice = tour.getTourPrices().stream()
             .filter(p -> Objects.equals(p.getId(), priceId))
                .findFirst();

        if(optionalTourPrice.isEmpty())
            return Result.failure("Price with id "+priceId+" do not exist");

        tour.removeTourPrice(optionalTourPrice.get()); // triggers orphan removal



        try {
            tour = tourRepository.save(tour);
        }
        catch (DataIntegrityViolationException e) {
            return Result.failure("Data integrity error: " + e.getMessage());
        }  catch (Exception e) {
            return Result.failure("Unexpected error: " + e.getMessage());
        }

        List<TourPrice> tourPrices = tour.getTourPrices();
        return Result.success(
                "price added",
                tourPrices
                        .stream()
                        .map(p->
                                new TourPriceDTO(
                                        p.getId(),
                                        p.getQuantity(),
                                        p.getPricePerPerson(),
                                        new CurrencyDTO(
                                                p.getCurrency().getCode(),
                                                p.getCurrency().getSymbol()
                                        )

                                )
                        ).toList()
        );
    }
}
