package com.webshop.domain.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import java.util.UUID;

@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class OrderNumber {
    @jakarta.persistence.Column(name = "order_number_value")
    String value;

    private OrderNumber(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be null or empty");
        }
        this.value = value;
    }

    public static OrderNumber create() {
        return new OrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }

    public static OrderNumber of(String value) {
        return new OrderNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}