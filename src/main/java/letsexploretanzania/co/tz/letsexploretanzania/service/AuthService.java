package letsexploretanzania.co.tz.letsexploretanzania.service;

import letsexploretanzania.co.tz.letsexploretanzania.common.enums.UserStatus;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.JwtUtils;
import letsexploretanzania.co.tz.letsexploretanzania.common.utils.Result;
import letsexploretanzania.co.tz.letsexploretanzania.constants.Constants;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.auth.JwtTokenDetails;
import letsexploretanzania.co.tz.letsexploretanzania.models.entities.User;
import letsexploretanzania.co.tz.letsexploretanzania.models.dto.responses.UserDTO;
import letsexploretanzania.co.tz.letsexploretanzania.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final OtpService otpService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AuthenticationManager  authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;



    public AuthService(OtpService otpService, EmailService emailService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder)
    {
      this.otpService = otpService;
      this.emailService = emailService;
      this.userRepository = userRepository;
      this.authenticationManager = authenticationManager;
      this.jwtUtils = jwtUtils;
      this.passwordEncoder = passwordEncoder;
    }

    public Result<String> sendOtp(String email)
    {
        String otp = otpService.generateOtp(email);
        try
        {
            emailService.sendGenericEmail(email,
              "Your OTP Code","Your OTP is: " + otp+" This will expire in 5 minutes",
              "<p>Your OTP is: <strong>" + otp + "</strong>. This will expire in 5 minutes.</p>");
            return Result.success(Constants.SUCCESS,Constants.SUCCESS);
        }catch (Exception e)
        {
            return Result.failure(e.getMessage());
        }
    }
    public Result<String> verifyOtp(String email, String otp)
    {
        try {
            boolean isValid = otpService.validateOtp(email, otp);
            if (isValid)
                return Result.success(Constants.SUCCESS,"Otp validated");
            return Result.failure("validation failed");
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<JwtTokenDetails> logIn(String email, String passWord)
    {
        try
        {
          Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, passWord));
          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          String token = jwtUtils.generateToken(
          userDetails.getUsername(),
          userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet())
          );

            return Result.success(
                "Login success",
                new JwtTokenDetails(
                        userDetails.getUsername(),
                        token
            ));

        }
        catch (BadCredentialsException e)
        {
            return Result.failure(e.getMessage());
        }
    }

    public Result<UserDTO> resetPassWord(String email, String password)
    {
        Optional<User> optionalUser = userRepository.findByUserName(email);
        if (optionalUser.isEmpty()) {
            return Result.failure("User with email " + email + " not found");
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));
        try {
            user =  userRepository.save(user);
            return Result.success(
              Constants.SUCCESS,
              new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                ""
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

    public Result<UserStatus> verifyUserByUserName(String email)
    {
        if (!userRepository.existsByUserName(email))
            return Result.success("success", UserStatus.NONEXISTENT);
        return Result.success("success", UserStatus.EXIST);
    }
}
