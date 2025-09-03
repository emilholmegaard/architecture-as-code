package com.webshop.domain.model;

import com.webshop.domain.model.vo.*;
import jakarta.persistence.*;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Currency;

/**
 * Order entity representing customer purchases.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderNumber orderNumber;

    private Long customerId;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "order_total_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "order_total_currency"))
    })
    private Money totalAmount;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    @Embedded
    private Address shippingAddress;

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED
    }

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long productId;
        private String productName;
        @Embedded
        private Quantity quantity;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "amount", column = @Column(name = "unit_price_amount")),
                @AttributeOverride(name = "currency", column = @Column(name = "unit_price_currency"))
        })
        private Money unitPrice;
        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "amount", column = @Column(name = "total_price_amount")),
                @AttributeOverride(name = "currency", column = @Column(name = "total_price_currency"))
        })
        private Money totalPrice;

        public void calculateTotalPrice() {
            this.totalPrice = this.unitPrice.multiply(this.quantity.getValue());
        }
    }

    /**
     * Calculates and updates the total order amount
     */
    public void calculateTotal() {
        // First calculate totals for each item
        items.forEach(OrderItem::calculateTotalPrice);

        // Then sum up all items
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Money.zero(Currency.getInstance("USD")), Money::add);
    }

    /**
     * Checks if order can be cancelled
     * 
     * @return true if order is cancellable
     */
    public boolean isCancellable() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }
}
