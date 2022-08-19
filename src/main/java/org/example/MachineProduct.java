package org.example;

import org.example.enums.ProductType;

public class MachineProduct {
    private final ProductType productType;
    private int stock;

    public MachineProduct(ProductType productType, int stock) {
        this.productType = productType;
        this.stock = stock;
    }

    public ProductType getProductType() {
        return productType;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
