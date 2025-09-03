package com.webshop.domain.service;

import com.webshop.domain.model.Product;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.ProductCategory;
import com.webshop.domain.model.vo.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void filterByPriceRange_WithinRange_ReturnsFilteredProducts() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product();
        product1.setPrice(Money.of(50.00, usd));

        Product product2 = new Product();
        product2.setPrice(Money.of(150.00, usd));

        Product product3 = new Product();
        product3.setPrice(Money.of(250.00, usd));

        List<Product> products = Arrays.asList(product1, product2, product3);
        Money minPrice = Money.of(40.00, usd);
        Money maxPrice = Money.of(200.00, usd);

        // Act
        List<Product> result = productService.filterByPriceRange(products, minPrice, maxPrice);

        // Assert
        assertThat(result).hasSize(2)
                .containsExactly(product1, product2);
    }

    @Test
    void requiresSpecialHandling_FragileProduct_ReturnsTrue() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Product product = new Product();
        product.setCategory(ProductCategory.ELECTRONICS); // Electronics is considered fragile
        product.setPrice(Money.of(50.00, usd));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void requiresSpecialHandling_ExpensiveProduct_ReturnsTrue() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Product product = new Product();
        product.setCategory(ProductCategory.CLOTHING); // Not fragile or special handling
        product.setPrice(Money.of(1500.00, usd));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void requiresSpecialHandling_RegularProduct_ReturnsFalse() {
        // Arrange
        Currency usd = Currency.getInstance("USD");
        Product product = new Product();
        product.setCategory(ProductCategory.CLOTHING); // Not fragile or special handling
        product.setPrice(Money.of(50.00, usd));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void calculateRestockQuantity_VeryLowStock_Returns100() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(Quantity.of(5));

        // Act
        Quantity result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result.getValue()).isEqualTo(100);
    }

    @Test
    void calculateRestockQuantity_LowStock_Returns50() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(Quantity.of(25));

        // Act
        Quantity result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result.getValue()).isEqualTo(50);
    }

    @Test
    void calculateRestockQuantity_AdequateStock_Returns0() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(Quantity.of(75));

        // Act
        Quantity result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result.getValue()).isEqualTo(0);
    }
}