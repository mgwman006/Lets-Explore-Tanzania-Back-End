package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.DeleteResponseDto;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.CurrencyEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TourDestinationEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.*;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddTourPriceDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourAddDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.*;
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
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public Result<TourCreatedDto> addTour(TourAddDto tourRequest)
    {
        Tour tour = new Tour(
                tourRequest.title(),
                tourRequest.overView(),
                tourRequest.durationDays(),
                tourRequest.hasSpecificDates()
        );

        //Add Tour Dates
        if (tour.isHasSpecificDates())
        {
            TourDate tourDate = new TourDate(
                    tourRequest.tourDates().startDate(),
                    tourRequest.tourDates().endDate()
            );
            tourDate.setTour(tour);
            tour.setTourDates(tourDate);
        }

        //Add Destinations
        for(String destination : tourRequest.destinations())
        {
            TourDestination tourDestination = new TourDestination(
                TourDestinationEnum.valueOf(destination.toUpperCase())
            );
            tourDestination.setTour(tour);
            tour.addDestination(tourDestination);
        }


        try {
            tour = tourRepository.save(tour);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        TourDate tourDate = tour.getTourDates();
        return Result.success("tour successfully created",
                new TourCreatedDto(
                tour.getId(),
                tour.getTitle(),
                tour.getOverView(),
                tour.getDurationDays(),
                tour.isHasSpecificDates(),
                tour.isHasSpecificDates() ?
                        new TourDateDTO(
                        tourDate.getStartDate(),
                        tourDate.getEndDate()
                        ): null,
                tour.getDestinations()
                        .stream()
                        .map(d-> d.getName().getName()).toList()
                )
        );
    }

    public Result<List<TourDetailsListItemDto>> getAllTours()
    {
        List<Tour> tours;
        try {
            tours = tourRepository.findAll();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        return Result
                .success(
                        "data fetch is successfully",
                        tours.stream()
                                .map(
                                        t -> new TourDetailsListItemDto(
                                                t.getId(),
                                                t.getTitle(),
                                                t.getOverView(),
                                                t.getDurationDays(),
                                                t.getBannerImageUrl(),
                                                t.isHasSpecificDates(),
                                                t.isHasSpecificDates() ?
                                                        new TourDateDTO(
                                                                t.getTourDates().getStartDate(),
                                                                t.getTourDates().getEndDate()
                                                        ):null,
                                                t.getDestinations()
                                                        .stream()
                                                        .map(d->d.getName().getName()).toList(),
                                                t.getTourPrices()
                                                        .stream()
                                                        .map(p-> new TourPriceDTO(
                                                                p.getId(),
                                                                p.getQuantity(),
                                                                p.getPricePerPerson(),
                                                                new CurrencyDTO(
                                                                        p.getCurrency().getCode(),
                                                                        p.getCurrency().getSymbol()
                                                                )
                                                        )).toList()
                                        )
                                ).toList()

                );
    }

    public Result<TourDetailsDto> getTourById(Long tourId) {

        Optional<Tour> optionalTour = tourRepository.findById(tourId);

        if (optionalTour.isEmpty())
        {
            return  Result.failure("Tour with id " + tourId + " not exist");
        }

        Tour tour = optionalTour.get();
        List<String> photos = tour.getPhotos().stream().map(Photo::getPhotoUrl).toList();
        TourDate tourDate = tour.getTourDates();
        return Result.success("success",
                new TourDetailsDto
                        (
                        tour.getId(),
                        tour.getTitle(),
                        tour.getOverView(),
                        tour.getDurationDays(),
                        tour.getBannerImageUrl(),
                        tour.isHasSpecificDates(),
                        tour.isHasSpecificDates()?
                        new TourDateDTO(
                                tourDate.getStartDate(),
                                tourDate.getEndDate()
                        ):null,
                        tour.getDestinations()
                                .stream()
                                .map(d-> d.getName().getName()).toList(),
                        tour.getTourPrices()
                                .stream()
                                .map( p ->
                                        new TourPriceDTO(
                                                p.getId(),
                                                p.getQuantity(),
                                                p.getPricePerPerson(),
                                                new CurrencyDTO(
                                                        p.getCurrency().getCode(),
                                                        p.getCurrency().getSymbol()
                                                )
                                        )).toList()
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

            String key = saveImageToS3(image,"tourimages/"+image.getOriginalFilename());
            tour.setBannerImageUrl(key);
            tour = tourRepository.save(tour);

            return Result.success("Banner added successfully", tour.getBannerImageUrl());
        } catch (Exception e) {

            return Result.failure(e.getMessage());
        }

    }

    public String saveImageToS3(MultipartFile image,String objectName) throws IOException {

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());


        String formattedNow = String.valueOf(now.getYear())+
                String.valueOf(now.getMonthValue())+
                String.valueOf(now.getDayOfMonth())+
                String.valueOf(now.getHour())+
                String.valueOf(now.getMinute())+
                String.valueOf(now.getSecond())+
                String.valueOf(now.getNano());

        System.out.println("yee");
        File tempFile = File.createTempFile("upload-", formattedNow+image.getOriginalFilename());
        image.transferTo(tempFile);
        System.out.println("yee 2");

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
                String imageUrl = saveImageToS3(photo,"tourimages/"+photo.getOriginalFilename());
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

        Tour tour = optionalTour.get();
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
}
