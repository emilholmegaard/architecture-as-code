package com.webshop.domain.model.vo;

public enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    BOOKS,
    HOME_AND_GARDEN,
    SPORTS,
    TOYS,
    BEAUTY,
    FOOD,
    AUTOMOTIVE,
    OTHER;

    public boolean isFragile() {
        return this == ELECTRONICS || this == HOME_AND_GARDEN;
    }

    public boolean requiresSpecialHandling() {
        return this == ELECTRONICS || this == FOOD;
    }

    public boolean isReturnable() {
        return this != FOOD;
    }
}