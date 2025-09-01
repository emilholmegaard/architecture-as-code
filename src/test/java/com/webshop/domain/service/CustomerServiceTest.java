package com.webshop.domain.service;

import com.webshop.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }

    @Test
    void getCustomerEmail_NonExistentCustomer_ReturnsEmpty() {
        // Act
        Optional<String> result = customerService.getCustomerEmail(1L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getCustomer_NonExistentCustomer_ReturnsEmpty() {
        // Act
        Optional<Customer> result = customerService.getCustomer(1L);

        // Assert
        assertThat(result).isEmpty();
    }
}