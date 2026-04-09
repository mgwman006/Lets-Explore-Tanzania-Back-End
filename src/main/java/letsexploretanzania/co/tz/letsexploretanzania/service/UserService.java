package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.RoleNameEnum;
import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.constants.Constants;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.requests.AddOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.CreatedOperatorDto;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.Role;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.TourOperator;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.User;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.OperatorDetailsDTO;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public Result<OperatorDetailsDTO> getOperator(String username)
    {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isEmpty())
        {
            return Result.failure("User with username " + username + " not found");
        }

        User user = optionalUser.get();
        TourOperator tourOperator = user.getTourOperator();

        if (tourOperator == null)
        {
            return Result.failure("No operator profile linked with username " + username);
        }

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
          Constants.SUCCESS,
          new OperatorDetailsDTO(
            tourOperator.getId(),
            tourOperator.getFirstName(),
            tourOperator.getLastName(),
            tourOperator.getPhone(),
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

//    public Result<CreatedOperatorDto> registerOperator(AddOperatorDto operatorDto)
//    {
//        if(userRepository.existsByUserName(operatorDto.email()))
//        {
//            return Result.failure("Email already registered");
//        }
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        User user = new User(
//          operatorDto.email(),
//          passwordEncoder.encode(operatorDto.passWord())
//        );
//        user.addRole(new Role(RoleNameEnum.OPERATOR));
//        TourOperator tourOperator = new TourOperator(
//          operatorDto.firstName(),
//          operatorDto.lastName(),
//          operatorDto.phone()
//        );
//
//        tourOperator.setUser(user);
//
//        try
//        {
//            tourOperator = tourOperatorRepository.save(tourOperator);
//            return Result.success(
//              "success",
//              new CreatedOperatorDto(
//                tourOperator.getId(),
//                tourOperator.getFirstName(),
//                tourOperator.getLastName(),
//                tourOperator.getUser().getUsername(),
//                tourOperator.getPhone()
//              )
//            );
//        }
//        catch (Exception e)
//        {
//            return  Result.failure(e.getMessage());
//        }
//    }
}
