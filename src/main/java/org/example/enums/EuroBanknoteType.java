package org.example.enums;

public enum EuroBanknoteType {
    HUNDRED_EURO(147 , 82,100),
    FIFTY_EURO(140 , 77,50),

    TWENTY_EURO(133 , 72,20),

    TEN_EURO(127 , 67,10),

    FIVE_EURO(120 , 62,5);

    private int length;
    private int whidth;
    private int value;

    EuroBanknoteType(int length, int whidth, int value) {
        this.length = length;
        this.whidth = whidth;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }

    public int getWhidth() {
        return whidth;
    }


}
