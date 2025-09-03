package com.webshop.domain.service;

import com.webshop.domain.model.Product;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.Quantity;
import com.webshop.domain.model.vo.ProductCategory;
import org.springframework.stereotype.Service;
import java.util.Currency;
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
     * 
     * @param products list of products
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return filtered products
     */
    public List<Product> filterByPriceRange(List<Product> products, Money minPrice, Money maxPrice) {
        return products.stream()
                .filter(p -> !p.getPrice().isLessThan(minPrice) && !p.getPrice().isGreaterThan(maxPrice))
                .collect(Collectors.toList());
    }

    /**
     * Checks if product requires special handling
     * 
     * @param product the product to check
     * @return true if special handling required
     */
    public boolean requiresSpecialHandling(Product product) {
        Money threshold = Money.of(1000.0, Currency.getInstance("USD"));
        return product.getCategory() != null &&
                (product.getCategory().isFragile() ||
                        product.getCategory().requiresSpecialHandling() ||
                        !product.getPrice().isLessThan(threshold));
    }

    /**
     * Calculates restock quantity based on current stock
     * 
     * @param product the product
     * @return suggested restock quantity
     */
    public Quantity calculateRestockQuantity(Product product) {
        Quantity lowThreshold = Quantity.of(10);
        Quantity mediumThreshold = Quantity.of(50);

        if (product.getStockQuantity().isLessThan(lowThreshold)) {
            return Quantity.of(100); // Restock with 100 units if very low
        } else if (product.getStockQuantity().isLessThan(mediumThreshold)) {
            return Quantity.of(50); // Restock with 50 units if low
        }
        return Quantity.zero(); // No restock needed
    }
}
