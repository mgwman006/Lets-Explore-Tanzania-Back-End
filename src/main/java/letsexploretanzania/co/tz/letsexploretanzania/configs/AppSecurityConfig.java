package letsexploretanzania.co.tz.letsexploretanzania.configs;

import letsexploretanzania.co.tz.letsexploretanzania.common.utils.JwtAuthenticationFilter;
import letsexploretanzania.co.tz.letsexploretanzania.configs.helpers.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class AppSecurityConfig
{
  private final JwtAuthenticationFilter jwtAuthFilter;

  public AppSecurityConfig(JwtAuthenticationFilter jwtAuthFilter)
  {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .cors(cors -> {})
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(configure ->
        configure
          .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          .requestMatchers("/api/v1/orders/**").permitAll()
          .requestMatchers("/api/v1/auth/**").permitAll()
          .requestMatchers("/api/v1/booking/**").permitAll()
          .requestMatchers(HttpMethod.GET,"/api/v1/tour/**").permitAll()
          .requestMatchers(HttpMethod.GET,"/api/v1/destination/**").permitAll()
          .requestMatchers(HttpMethod.GET,"/api/v1/currency/**").permitAll()
          .anyRequest().authenticated()
      );

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
  {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties)
  {
    CorsConfiguration configuration = new CorsConfiguration();

    //Allowed Origins
    configuration.setAllowCredentials(corsProperties.isAllowCredentials());
    configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
    configuration.setAllowedMethods(corsProperties.getAllowedMethods());

    //Headers
    configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
    configuration.setExposedHeaders(corsProperties.getExposedHeaders());

    //Map url with configurations
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
