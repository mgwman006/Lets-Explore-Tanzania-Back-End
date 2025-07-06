package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserType;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourOperator;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.User;
import letsexploretanzania.co.tz.letsexploretanzania.models.requests.AddOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.CreatedOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TourOperatorRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourOperatorService {

    private final TourOperatorRepository tourOperatorRepository;

    @Autowired
    public TourOperatorService(TourOperatorRepository tourOperatorRepository) {
        this.tourOperatorRepository = tourOperatorRepository;
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
}
