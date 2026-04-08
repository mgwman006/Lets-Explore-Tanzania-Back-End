package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.RoleNameEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Role;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourOperator;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.User;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
    }

    public Result<OperatorDetailsDTO> getOperator(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return Result.failure("User with id " + userId + " not found");
        }

        if (!optionalUser.get().getRoles().contains(new Role(RoleNameEnum.OPERATOR))) {
            return Result.failure("User with id " + userId + " is NOT a TOUROPERATOR");
        }

        TourOperator tourOperator = optionalUser.get().getTourOperator();

        return Result.success(
                "success",
                new OperatorDetailsDTO(
                        tourOperator.getId(),
                        tourOperator.getFirstName(),
                        tourOperator.getLastName(),
                        tourOperator.getUser().getUsername(),
                        tourOperator.getPhone(),
                        tourOperator.getBookings().size(),
                        tourOperator.getTours().size()
                )
        );
    }

    public Result<UserDTO> registerUser(String email, String rawPassword, List<String> incomingRoles)
    {
        if (userRepository.existsByUserName(email))
        {
            return Result.failure("User with this email already exists");
        }

        try
        {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User(email, encodedPassword);

            for (String name : incomingRoles)
            {
                RoleNameEnum roleName = RoleNameEnum.getFromName(name);
                if (!RoleNameEnum.INVALID.equals(roleName))
                {
                    Role role = new Role(roleName);
                    user.addRole(role);
                }
            }

            user = userRepository.save(user);
            return Result.success(
              "success",
              new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                ""
              )
            );
        }
        catch (Exception exception)
        {
            return Result.failure(exception.getMessage());
        }
    }
}
