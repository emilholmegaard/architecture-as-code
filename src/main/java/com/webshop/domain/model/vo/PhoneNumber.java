package com.webshop.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import java.util.regex.Pattern;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PhoneNumber {
    @jakarta.persistence.Column(name = "phone_value")
    String value;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        String normalized = phoneNumber.replaceAll("[\\s-()]", "");
        if (!PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        return new PhoneNumber(normalized);
    }

    @Override
    public String toString() {
        return value;
    }
}