package com.webshop.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.ProductCategory;
import com.webshop.domain.model.vo.Quantity;
import com.webshop.domain.model.vo.SKU;

/**
 * Product entity representing items in the web shop catalog.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private SKU sku;
    private String name;
    private String description;
    @Embedded
    private Money price;
    @Embedded
    private Quantity stockQuantity;
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Checks if the product is available for purchase
     * 
     * @return true if stock is available
     */
    public boolean isAvailable() {
        return stockQuantity != null && !stockQuantity.isZero();
    }

    /**
     * Reduces stock quantity after purchase
     * 
     * @param quantity amount to reduce
     * @throws IllegalStateException if insufficient stock
     */
    public void reduceStock(Quantity quantity) {
        if (stockQuantity.isLessThan(quantity)) {
            throw new IllegalStateException("Insufficient stock for product: " + sku);
        }
        stockQuantity = stockQuantity.subtract(quantity);
    }
}
