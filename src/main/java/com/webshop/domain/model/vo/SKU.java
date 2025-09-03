package com.webshop.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class SKU {
    @jakarta.persistence.Column(name = "sku_value")
    String value;

    private static final String SKU_PATTERN = "^[A-Z0-9]{8,15}$";

    private SKU(String value) {
        this.value = value;
    }

    public static SKU of(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }

        String normalized = sku.toUpperCase().trim();
        if (!normalized.matches(SKU_PATTERN)) {
            throw new IllegalArgumentException("Invalid SKU format. Must be 8-15 alphanumeric characters");
        }
        return new SKU(normalized);
    }

    @Override
    public String toString() {
        return value;
    }
}