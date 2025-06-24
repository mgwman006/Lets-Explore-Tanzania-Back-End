package letsexploretanzania.co.tz.letsexploretanzania.service;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.DeleteResponseDto;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.CurrencyEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddTourPriceDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourActivityAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.privatetour.PrivateTourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourCreatedDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.privatetour.PrivateTourDetailsListItemDto;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourGuideRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PrivateTourService {

    private final TourRepository tourRepository;
    private final TourGuideRepository tourGuideRepository;

    public PrivateTourService(TourRepository tourRepository, TourGuideRepository tourGuideRepository) {
        this.tourRepository = tourRepository;
        this.tourGuideRepository = tourGuideRepository;
    }

    public Result<PrivateTourCreatedDto> addTour(PrivateTourAddDto tourRequest, MultipartFile bannerImage) throws IOException {
        PrivateTour tour = new PrivateTour(
                tourRequest.title(),
                tourRequest.overView(),
                tourRequest.durationDays()
        );


        //Add Destinations
        for(String destination : tourRequest.destinations())
        {
            TourDestination tourDestination = new TourDestination(
                TourDestinationEnum.valueOf(destination.toUpperCase())
            );
            tourDestination.setTour(tour);
            tour.addDestination(tourDestination);
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        String formattedNow = String.valueOf(now.getYear())+
                String.valueOf(now.getMonthValue())+
                String.valueOf(now.getDayOfMonth())+
                String.valueOf(now.getHour())+
                String.valueOf(now.getMinute())+
                String.valueOf(now.getSecond())+
                String.valueOf(now.getNano());

        String objectName = formattedNow+"banners/"+bannerImage.getOriginalFilename();

        String key = saveImageToS3(bannerImage,objectName);
        tour.setBannerImageUrl(key);

        try {
            tour = tourRepository.save(tour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        return Result.success("tour successfully created",
                new PrivateTourCreatedDto(
                tour.getId(),
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

            String key = saveImageToS3(image,objectName);
            tour.setBannerImageUrl(key);
            tour = tourRepository.save(tour);

            return Result.success("Banner added successfully", tour.getBannerImageUrl());
        } catch (Exception e) {

            return Result.failure(e.getMessage());
        }

    }

    public String saveImageToS3(MultipartFile image,String objectName) throws IOException {


        File tempFile = File.createTempFile("upload-", image.getOriginalFilename());
        image.transferTo(tempFile);


        Path path = tempFile.toPath();
        String bucketName = "letsexploretanzania";
        final String region = "eu-west-2"; // Replace with your region
        S3Client s3Client = S3Client.builder().region(Region.of(region)).build();


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .contentType(Files.probeContentType(path))
                .build();


        try {

            s3Client.putObject(putObjectRequest, path);
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + objectName;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload image to S3", e);
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
                String imageUrl = saveImageToS3(photo,objectName);
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

    public Result<TourGuideDTO> addTourGuide(Long tourId, MeetingPoint pickingUpInformation) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty())
            return Result.failure("Tour with id "+tourId+" do not exist");

        Tour tour = optionalTour.get();

        TourGuide tourGuide = new TourGuide(pickingUpInformation);
        tourGuide.setTour(tour);
        tour.setGuide(tourGuide);

        try
        {
            tour = tourRepository.save(tour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
        tourGuide = tour.getGuide();

        return Result.success(
                "success",
                new TourGuideDTO(
                        tourGuide.getId(),
                        tourGuide.getPickUpInformation(),
                        tourGuide.getEndOfTourInformation(),
                        new ArrayList<TourActivityDetailsDTO>()
                )
        );
    }


    public Result<List<TourActivityDetailsDTO>> addTourActivity(
            Long tourGuideId,
            TourActivityAddDTO tourActivityDTO,
            List<MultipartFile> photos
    ) {

        Optional<TourGuide> optionalTourGuide = tourGuideRepository.findById(tourGuideId);
        if (optionalTourGuide.isEmpty())
            return Result.failure("TourGuide with id "+tourGuideId+" not exist");
        TourGuide tourGuide = optionalTourGuide.get();

        TourActivity tourActivity = new TourActivity(
                tourActivityDTO.dayNumber(),
                tourActivityDTO.title(),
                tourActivityDTO.description(),
                tourActivityDTO.location(),
                tourActivityDTO.startTime(),
                tourActivityDTO.endTime()
        );

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

                String objectName = formattedNow+"activityImages/"+photo.getOriginalFilename();

                String imageUrl = saveImageToS3(photo,objectName);
                Photo newPhoto = new Photo(imageUrl);
                newPhoto.setTourActivity(tourActivity);
                tourActivity.addPhoto(newPhoto);
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        }

        tourActivity.setTourGuide(tourGuide);
        tourGuide.addTourActivity(tourActivity);

        try
        {
            tourGuide = tourGuideRepository.save(tourGuide);
        } catch (Exception e) {
            return Result.failure("Save Failed "+e.getMessage());
        }


        List<TourActivity> tourActivities = tourGuide.getTourActivities();

        return Result.success(
                "success",
                tourActivities
                        .stream()
                        .map( a ->
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
                                                .map(p -> p.getPhotoUrl())
                                                .toList()
                                )
                        ).toList()
        );

    }

    public Result<MeetingPoint> addEndOfTourInformation(Long tourGuideId, MeetingPoint endOfTourInformation)
    {
        Optional<TourGuide> optionalTourGuide = tourGuideRepository.findById(tourGuideId);
        if (optionalTourGuide.isEmpty())
            return Result.failure("Tour with id "+tourGuideId+" do not exist");

        TourGuide tourGuide = optionalTourGuide.get();

        tourGuide.setEndOfTourInformation(endOfTourInformation);


        try
        {
            tourGuide = tourGuideRepository.save(tourGuide);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
        MeetingPoint newEndOfTourPoint = tourGuide.getEndOfTourInformation();

        return Result.success(
                "success",
                endOfTourInformation
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
}
