package org.example.enums;

public enum EuroMoneyType {
    HUNDRED_EURO(10000),

    FIFTY_EURO(5000),

    TWENTY_EURO(2000),

    TEN_EURO(1000),

    FIVE_EURO(500),
    TWO_EURO(200),
    ONE_EURO(100),
    FIFTY_CENT(50),
    TWENTY_CENT(20),
    TEN_CENT(10),
    FIVE_CENT(5),
    TWO_CENT(2),
    ONE_CENT(1);

    private final int value;


    EuroMoneyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
