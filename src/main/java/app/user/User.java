package app.user;

import lombok.*;

@Value // All fields are private and final. Getters (but not setters) are generated (https://projectlombok.org/features/Value.html)
public class User {
    String name;
    String username;
    String hashedPassword;
    String salt;
    String role;
}
