package org.example.enums;

public enum ProductType {
    COCA_COLA(130),
    FANTA(120),
    SPRITE(125),
    WATER(119),
    TONIC_WATER(167),
    LAYS(80),
    CHIO_CHIPS(80),

    PEANUTS(60),

    SKITTLES(55),

    BAKE_ROLLS(268),
    DORITOS(60),
    M_AND_M(55),
    MASK(200),
    GUM(100),
    KENT(300),
    MAGNET(1500),
    DUNHILL(300),
    KEY_HOLDER(3200);

    private int price;


    ProductType(int price) {
        this.price = price;

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
