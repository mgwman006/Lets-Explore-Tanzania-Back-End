package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TouristStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tourist;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TouristService {
    private final TouristRepository touristRepository;

    public TouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public Result<TouristStatus> verifyTouristByEmail(String email)
    {
        Optional<Tourist> optionalTourist = touristRepository.findByEmail(email);
        if (optionalTourist.isEmpty())
            return Result.success("success", TouristStatus.NONEXISTENT);
        return Result.success("success",TouristStatus.EXIST);
    }
}
