package com.webshop.domain.model;

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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private CustomerType type;
    private LocalDateTime registrationDate;
    private boolean active;
    
    public enum CustomerType {
        REGULAR, PREMIUM, VIP
    }
    
    /**
     * Gets the full name of the customer
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Checks if customer is eligible for premium benefits
     * @return true if premium or VIP
     */
    public boolean hasPremiumBenefits() {
        return type == CustomerType.PREMIUM || type == CustomerType.VIP;
    }
}
