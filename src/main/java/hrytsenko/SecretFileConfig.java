package hrytsenko;

import io.smallrye.config.ConfigSourceInterceptor;
import io.smallrye.config.ConfigSourceInterceptorContext;
import io.smallrye.config.ConfigValue;
import io.smallrye.config.Priorities;
import jakarta.annotation.Priority;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Priority(Priorities.APPLICATION)
public class SecretFileConfig implements ConfigSourceInterceptor {

  private static final String SECRET_FILE_PREFIX = "secret-file:";

  @SneakyThrows
  @Override
  public ConfigValue getValue(ConfigSourceInterceptorContext context, String name) {
    var property = context.proceed(name);
    if (property == null || !property.getValue().startsWith(SECRET_FILE_PREFIX)) {
      return property;
    }
    log.info("Secret property '{}' with value '{}'", property.getName(), property.getValue());

    var value = property.getValue().substring(SECRET_FILE_PREFIX.length());
    var path = Paths.get(value);
    if (!Files.exists(path)) {
      log.error("Cannot read secret from file '{}'", value);
      return property;
    }
    var secret = Files.readString(path);
    return property.withValue(secret);
  }

}
