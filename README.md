# Quarkus Config Secret

Load secrets from files using [Quarkus Config](https://quarkus.io/guides/config). 

## Example

Declare configuration properties as follows:

```yaml
credentials:
  username: secret-file:${USERNAME_FILE}
  password: secret-file:${PASSWORD_FILE}
```

Where `USERNAME_FILE` and `PASSWORD_FILE` are environment variables that point to the secret files.

Inject configuration properties as follows:

```java
@ConfigProperty(name = "credentials.username")
String username;
@ConfigProperty(name = "credentials.password")
String password;
```
