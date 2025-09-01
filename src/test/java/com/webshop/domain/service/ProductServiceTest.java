package com.webshop.domain.service;

import com.webshop.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
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
        Product product1 = new Product();
        product1.setPrice(new BigDecimal("50.00"));

        Product product2 = new Product();
        product2.setPrice(new BigDecimal("150.00"));

        Product product3 = new Product();
        product3.setPrice(new BigDecimal("250.00"));

        List<Product> products = Arrays.asList(product1, product2, product3);
        BigDecimal minPrice = new BigDecimal("40.00");
        BigDecimal maxPrice = new BigDecimal("200.00");

        // Act
        List<Product> result = productService.filterByPriceRange(products, minPrice, maxPrice);

        // Assert
        assertThat(result).hasSize(2)
                .containsExactly(product1, product2);
    }

    @Test
    void requiresSpecialHandling_FragileProduct_ReturnsTrue() {
        // Arrange
        Product product = new Product();
        product.setCategory("FRAGILE");
        product.setPrice(new BigDecimal("50.00"));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void requiresSpecialHandling_ExpensiveProduct_ReturnsTrue() {
        // Arrange
        Product product = new Product();
        product.setCategory("NORMAL");
        product.setPrice(new BigDecimal("1500.00"));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void requiresSpecialHandling_RegularProduct_ReturnsFalse() {
        // Arrange
        Product product = new Product();
        product.setCategory("NORMAL");
        product.setPrice(new BigDecimal("50.00"));

        // Act
        boolean result = productService.requiresSpecialHandling(product);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void calculateRestockQuantity_VeryLowStock_Returns100() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(5);

        // Act
        int result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result).isEqualTo(100);
    }

    @Test
    void calculateRestockQuantity_LowStock_Returns50() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(25);

        // Act
        int result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result).isEqualTo(50);
    }

    @Test
    void calculateRestockQuantity_AdequateStock_Returns0() {
        // Arrange
        Product product = new Product();
        product.setStockQuantity(75);

        // Act
        int result = productService.calculateRestockQuantity(product);

        // Assert
        assertThat(result).isEqualTo(0);
    }
}