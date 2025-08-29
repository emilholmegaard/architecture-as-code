package com.webshop.domain.service;

import com.webshop.domain.model.Product;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain service for product business logic.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Service
public class ProductService {
    
    /**
     * Filters products by price range
     * @param products list of products
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return filtered products
     */
    public List<Product> filterByPriceRange(List<Product> products, BigDecimal minPrice, BigDecimal maxPrice) {
        return products.stream()
            .filter(p -> p.getPrice().compareTo(minPrice) >= 0 && p.getPrice().compareTo(maxPrice) <= 0)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if product requires special handling
     * @param product the product to check
     * @return true if special handling required
     */
    public boolean requiresSpecialHandling(Product product) {
        return product.getCategory() != null && 
            (product.getCategory().equals("FRAGILE") || 
             product.getCategory().equals("HAZARDOUS") ||
             product.getPrice().compareTo(new BigDecimal("1000")) > 0);
    }
    
    /**
     * Calculates restock quantity based on current stock
     * @param product the product
     * @return suggested restock quantity
     */
    public int calculateRestockQuantity(Product product) {
        if (product.getStockQuantity() < 10) {
            return 100; // Restock with 100 units if very low
        } else if (product.getStockQuantity() < 50) {
            return 50; // Restock with 50 units if low
        }
        return 0; // No restock needed
    }
}
