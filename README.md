# Quarkus Config Secret

Extension for Quarkus Config to read secrets from files. 

## Example

```yaml
credentials:
  username: secret-file:${USERNAME_FILE}
  password: secret-file:${PASSWORD_FILE}
```

Where `USERNAME_FILE` and `PASSWORD_FILE` are environment variables that point to the respective files.
