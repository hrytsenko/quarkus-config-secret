package hrytsenko;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import java.nio.file.Files;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
@TestProfile(SecretFileConfigQuarkusTest.SecretProfile.class)
class SecretFileConfigQuarkusTest {

  public static class SecretProfile implements QuarkusTestProfile {

    @SneakyThrows
    @Override
    public Map<String, String> getConfigOverrides() {
      var secretFile = Files.createTempFile("password", "");
      Files.writeString(secretFile, "P@ssw0rd");
      return Map.of("PASSWORD_FILE", secretFile.toString());
    }
  }

  @ConfigProperty(name = "secret.password")
  String password;

  @Test
  void readSecret() {
    assertEquals("P@ssw0rd", password);
  }

}
