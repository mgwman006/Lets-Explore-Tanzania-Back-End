package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.TouristStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Tourist;
import letsexploretanzania.co.tz.letsexploretanzania.repository.TouristRepository;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TouristService {
    private final UserRepository userRepository;
    private final TouristRepository touristRepository;

    public TouristService(UserRepository userRepository, TouristRepository touristRepository) {
      this.userRepository = userRepository;
      this.touristRepository = touristRepository;
    }

    public Result<TouristStatus> verifyTouristByEmail(String email)
    {
        if (!userRepository.existsByEmail(email))
            return Result.success("success", TouristStatus.NONEXISTENT);
        return Result.success("success",TouristStatus.EXIST);
    }
}
