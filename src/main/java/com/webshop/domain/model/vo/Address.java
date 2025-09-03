package com.webshop.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Address {
    String street;
    String city;
    String zipCode;
    String country;

    private Address(String street, String city, String zipCode, String country) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }

        this.street = street.trim();
        this.city = city.trim();
        this.zipCode = zipCode.trim();
        this.country = country.trim();
    }

    public static Address of(String street, String city, String zipCode, String country) {
        return new Address(street, city, zipCode, country);
    }

    @Override
    public String toString() {
        return String.format("%s, %s %s, %s", street, city, zipCode, country);
    }
}