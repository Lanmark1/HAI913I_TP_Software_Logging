package com.fds.softlog.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SearchedProduct {

    private Product product;
    private LocalDateTime timestamp;

    public SearchedProduct(Product product, LocalDateTime timestamp) {
        this.product = product;
        this.timestamp = timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
