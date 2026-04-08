package letsexploretanzania.co.tz.letsexploretanzania.configs;

import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.event.Level;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class GlobalConfig
{
  @Value("${paypal.client.id}")
  private String paypalClientId;
  @Value("${paypal.client.secret}")
  private String paypalClientSecret;
  @Value("${paypal.env}")
  private String environment;

  @Bean
  public PaypalServerSdkClient paypalClient()
  {
    return new PaypalServerSdkClient.Builder()
      .loggingConfig(builder -> builder
        .level(Level.DEBUG)
        .requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
        .responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
      .httpClientConfig(configBuilder -> configBuilder
        .timeout(0))
      .environment(Environment.valueOf(environment))
      .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
          paypalClientId,
          paypalClientSecret
        ).build()
      )
      .build();
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory)
  {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);

    // Use String serialization for both keys and values
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    template.setKeySerializer(stringSerializer);
    template.setValueSerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);
    template.setHashValueSerializer(stringSerializer);

    template.afterPropertiesSet(); // Initialize the template
    return template;
  }

}
