package com.example.cosmetics.entity;

public enum PaymentMethod {
    BANK_TRANSFER("Bank_transfer"),
    COD("Cod");

    private String value;
    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
