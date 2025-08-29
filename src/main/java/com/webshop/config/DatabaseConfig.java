package com.webshop.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration for the web shop application.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.webshop.infrastructure.persistence")
@EntityScan(basePackages = "com.webshop.domain.model")
public class DatabaseConfig {
    // Additional database configuration can be added here
    // Connection pool settings, dialect configuration, etc.
}
