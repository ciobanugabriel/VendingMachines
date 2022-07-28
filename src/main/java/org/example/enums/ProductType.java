package org.example.enums;

public enum ProductType {
    COCA_COLA(156,"A01"),
    FANTA(142,"A02"),
    SPRITE(133,"A03"),
    WATER(119,"A04"),
    TONIC_WATER(167,"A05"),
    LAYS(225,"B01"),
    CHIO_CHIPS(247,"B02"),
    BAKE_ROLLS(268,"B03"),
    DORITOS(277,"B04"),
    M_AND_M(141,"B05"),
    MASK(500,"C01"),
    KENT(1500,"C02"),
    MAGNET(2500,"C03"),
    DUNHILL(7500,"C04"),
    KEY_HOLDER(3500,"C05");

    private int price;
    private final String machineCode;

    ProductType(int price, String machineCode) {
        this.price = price;
        this.machineCode = machineCode;
    }

    public int getPrice() {
        return price;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
