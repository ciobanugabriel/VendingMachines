package org.example.enums;

public enum EuroCoinType {
    TWO_EURO(200,25.75,2.20),
    ONE_EURO(100,23.25,2.33),
    FIFTY_CENT(50,24.25,2.38),
    TWENTY_CENT(20,22.25,2.14),
    TEN_CENT(10,19.75,1.93),
    FIVE_CENT(5,21.25,1.67),
    TWO_CENT(2,18.75,1.67),
    ONE_CENT(1,16.25,1.67);

    private int value;
    private double diameter;
    private double thickness;

    EuroCoinType(int value, double diameter, double thickness) {
        this.value = value;
        this.diameter = diameter;
        this.thickness = thickness;
    }

    public int getValue() {
        return value;
    }

    public double getDiameter() {
        return diameter;
    }

    public double getThickness() {
        return thickness;
    }
}
