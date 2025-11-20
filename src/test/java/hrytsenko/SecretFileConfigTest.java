package hrytsenko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import io.smallrye.config.ConfigSourceInterceptorContext;
import io.smallrye.config.ConfigValue;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SecretFileConfigTest {

  SecretFileConfig interceptor;
  ConfigSourceInterceptorContext context;

  @BeforeEach
  void init() {
    interceptor = new SecretFileConfig();
    context = mock(ConfigSourceInterceptorContext.class);
  }

  @SneakyThrows
  @Test
  void readSecret_fileIsPresent() {
    var sourceFile = Files.createTempFile("password", "");
    Files.writeString(sourceFile, "P@ssw0rd");

    var sourceProperty = ConfigValue.builder()
        .withName("password")
        .withValue("secret-file:" + sourceFile)
        .build();
    doReturn(sourceProperty).when(context).proceed("password");

    var actualProperty = interceptor.getValue(context, "password");

    assertEquals("P@ssw0rd", actualProperty.getValue());
  }

  @Test
  void readSecret_fileIsAbsent() {
    var sourceProperty = ConfigValue.builder()
        .withName("password")
        .withValue("secret-file:password.txt")
        .build();
    doReturn(sourceProperty).when(context).proceed("password");

    var actualProperty = interceptor.getValue(context, "password");

    assertSame(sourceProperty, actualProperty);
  }

}
