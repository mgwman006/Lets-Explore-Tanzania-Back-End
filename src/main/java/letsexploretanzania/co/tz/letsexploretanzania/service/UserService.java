package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserType;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourOperator;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.User;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<OperatorDetailsDTO> getOperator(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return Result.failure("User with id " + userId + " not found");
        }

        if (optionalUser.get().getUserType() != UserType.TOUROPERATOR) {
            return Result.failure("User with id " + userId + " is NOT a TOUROPERATOR");
        }

        TourOperator tourOperator = optionalUser.get().getTourOperator();

        return Result.success(
                "success",
                new OperatorDetailsDTO(
                        tourOperator.getId(),
                        tourOperator.getFirstName(),
                        tourOperator.getLastName(),
                        tourOperator.getEmail(),
                        tourOperator.getPhone()
                )
        );
    }

    public Result<UserDTO> resetPassWord(String email, String password)
    {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return Result.failure("User with email " + email + " not found");
        }

        User user = optionalUser.get();
        user.setPassword(password);
        try {
            user =  userRepository.save(user);
            return Result.success(
                    "success",
                    new UserDTO(
                            user.getId(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getUserType().getName()
                    )
            );
        }
        catch (Exception e)
        {
            return Result.failure(
                    e.getMessage()
            );
        }
    }
}
