package com.webshop.infrastructure.persistence;

import com.webshop.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void whenSaveProduct_thenProductIsPersisted() {
        // Create a test product
        Product product = Product.builder()
                .sku("TEST-SKU-001")
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("19.99"))
                .stockQuantity(100)
                .category("TEST")
                .createdAt(LocalDateTime.now())
                .build();

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Verify the product was saved
        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getSku()).isEqualTo("TEST-SKU-001");
        assertThat(foundProduct.getStockQuantity()).isEqualTo(100);
    }
}