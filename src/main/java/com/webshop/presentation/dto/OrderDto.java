package com.webshop.presentation.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDto {
    private Long id;
    private Long customerId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
}