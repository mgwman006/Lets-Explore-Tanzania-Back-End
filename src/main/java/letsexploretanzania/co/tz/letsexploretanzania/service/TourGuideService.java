package letsexploretanzania.co.tz.letsexploretanzania.service;

import jakarta.validation.Valid;
import letsexploretanzania.co.tz.letsexploretanzania.common.dtos.MeetingPoint;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Photo;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourActivity;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourGuide;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.TourActivityAddDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourActivityDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.TourGuideDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourGuideRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class TourGuideService {
    private final TourGuideRepository tourGuideRepository;
    private final AWSService awsService;

    public TourGuideService(TourGuideRepository tourGuideRepository, AWSService awsService) {
        this.tourGuideRepository = tourGuideRepository;
        this.awsService = awsService;
    }


    public Result<TourGuideDTO> addEndOfTourInformation(Long tourGuideId, MeetingPoint endOfTourInformation)
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

        return Result.success(
                "success",
                new TourGuideDTO(
                        tourGuide.getId(),
                        tourGuide.getPickUpInformation(),
                        tourGuide.getEndOfTourInformation(),
                        tourGuide.getTourActivities()
                                .stream()
                                .map(a -> new TourActivityDetailsDTO(
                                        a.getId(),
                                        a.getDayNumber(),
                                        a.getTitle(),
                                        a.getDescription(),
                                        a.getLocation(),
                                        a.getStartTime(),
                                        a.getEndTime(),
                                        a.getPhotos().stream().map(p -> p.getPhotoUrl()).toList()
                                )).toList()
                )
        );

    }

    public Result<TourGuideDTO> addTourGuide(Long tourGuideId, MeetingPoint pickingUpInformation) {
        Optional<TourGuide> optionalTourGuide = tourGuideRepository.findById(tourGuideId);
        if (optionalTourGuide.isEmpty())
            return Result.failure("Tour with id "+tourGuideId+" do not exist");

        TourGuide tourGuide = optionalTourGuide.get();

        tourGuide.setPickUpInformation(pickingUpInformation);

        try
        {
            tourGuide = tourGuideRepository.save(tourGuide);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }

        return Result.success(
                "success",
                new TourGuideDTO(
                        tourGuide.getId(),
                        tourGuide.getPickUpInformation(),
                        tourGuide.getEndOfTourInformation(),
                        tourGuide.getTourActivities()
                                .stream()
                                .map(a -> new TourActivityDetailsDTO(
                                        a.getId(),
                                        a.getDayNumber(),
                                        a.getTitle(),
                                        a.getDescription(),
                                        a.getLocation(),
                                        a.getStartTime(),
                                        a.getEndTime(),
                                        a.getPhotos().stream().map(p -> p.getPhotoUrl()).toList()
                                )).toList()
                )
        );
    }


    public Result<TourGuideDTO> addTourActivity(
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

                String imageUrl = awsService.saveImageToS3(photo, objectName);
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
                new TourGuideDTO(
                        tourGuide.getId(),
                        tourGuide.getPickUpInformation(),
                        tourGuide.getEndOfTourInformation(),
                        tourGuide.getTourActivities()
                                .stream()
                                .map(a -> new TourActivityDetailsDTO(
                                        a.getId(),
                                        a.getDayNumber(),
                                        a.getTitle(),
                                        a.getDescription(),
                                        a.getLocation(),
                                        a.getStartTime(),
                                        a.getEndTime(),
                                        a.getPhotos().stream().map(p -> p.getPhotoUrl()).toList()
                                )).toList()
                )
        );

    }









}
