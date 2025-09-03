package com.webshop.infrastructure.persistence;

import com.webshop.domain.model.Product;
import com.webshop.domain.model.vo.Money;
import com.webshop.domain.model.vo.ProductCategory;
import com.webshop.domain.model.vo.Quantity;
import com.webshop.domain.model.vo.SKU;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Currency;

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
        Currency usd = Currency.getInstance("USD");
        Product product = Product.builder()
                .sku(SKU.of("TESTSKU001")) // Changed to match the required pattern ^[A-Z0-9]{8,15}$
                .name("Test Product")
                .description("Test Description")
                .price(Money.of(19.99, usd))
                .stockQuantity(Quantity.of(100))
                .category(ProductCategory.ELECTRONICS)
                .createdAt(LocalDateTime.now())
                .build();

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Verify the product was saved
        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getSku().getValue()).isEqualTo("TESTSKU001");
        assertThat(foundProduct.getStockQuantity().getValue()).isEqualTo(100);
    }
}