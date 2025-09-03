package com.webshop.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import java.util.regex.Pattern;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class EmailAddress {
    @jakarta.persistence.Column(name = "email_value")
    String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private EmailAddress(String value) {
        this.value = value;
    }

    public static EmailAddress of(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return new EmailAddress(email.toLowerCase().trim());
    }

    @Override
    public String toString() {
        return value;
    }
}