package com.webshop.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Customer entity representing web shop users.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    @Enumerated(EnumType.STRING)
    private CustomerType type;
    private LocalDateTime registrationDate;
    private boolean active;

    public enum CustomerType {
        REGULAR, PREMIUM, VIP
    }

    /**
     * Gets the full name of the customer
     * 
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if customer is eligible for premium benefits
     * 
     * @return true if premium or VIP
     */
    public boolean hasPremiumBenefits() {
        return type == CustomerType.PREMIUM || type == CustomerType.VIP;
    }
}
