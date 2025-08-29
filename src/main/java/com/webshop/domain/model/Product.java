package com.webshop.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing items in the web shop catalog.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Checks if the product is available for purchase
     * @return true if stock is available
     */
    public boolean isAvailable() {
        return stockQuantity != null && stockQuantity > 0;
    }
    
    /**
     * Reduces stock quantity after purchase
     * @param quantity amount to reduce
     * @throws IllegalStateException if insufficient stock
     */
    public void reduceStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + sku);
        }
        stockQuantity -= quantity;
    }
}
