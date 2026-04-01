package hrytsenko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    var sourceValue = "secret-file:" + sourceFile;
    var actualConfig = handlePassword(sourceValue);

    assertEquals("P@ssw0rd", actualConfig.getValue());
  }

  @Test
  void readSecret_fileIsAbsent() {
    var sourceValue = "secret-file:password.txt";
    var actualConfig = handlePassword(sourceValue);

    assertEquals(sourceValue, actualConfig.getValue());
  }

  @Test
  void readSecret_valueIsAbsent() {
    var actualProperty = handlePassword(null);

    assertNull(actualProperty.getValue());
  }

  ConfigValue handlePassword(String value) {
    String name = "password";
    var sourceProperty = ConfigValue.builder()
        .withName(name)
        .withValue(value)
        .build();
    doReturn(sourceProperty).when(context).proceed(name);

    return interceptor.getValue(context, name);
  }

}
