package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public class JuicesVendingMachine extends VendingMachine {


    public JuicesVendingMachine(User user, boolean admin) {
        this.user = user;
        this.admin = admin;

        productInventory = new EnumMap<>(ProductType.class);
        productInventory.put(ProductType.COCA_COLA, 5);
        productInventory.put(ProductType.FANTA, 5);
        productInventory.put(ProductType.SPRITE, 5);
        productInventory.put(ProductType.WATER, 5);
        productInventory.put(ProductType.TONIC_WATER, 5);

        moneyInventory = new EnumMap<>(EuroMoneyType.class);
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            if (!moneyTypeIsNotValid(moneyType)) {
                moneyInventory.put(moneyType, 10);
            }
        }
    }


    @Override
    public boolean moneyTypeIsNotValid(EuroMoneyType moneyType) {
        return switch (moneyType) {
            case ONE_CENT -> false;
            case TWO_CENT -> false;
            case FIVE_CENT -> false;
            case TEN_CENT -> false;
            case TWENTY_CENT -> false;
            case FIFTY_CENT -> false;
            case ONE_EURO -> false;
            case TWO_EURO -> false;
            default -> true;
        };
    }


    @Override
    public void setMoneyInventory(int oneCentAmount, int twoCentAmount, int fiveCentAmount, int tenCentAmount,
                                  int twentyCentAmount, int fiftyCentAmount, int oneEuroCentAmount, int twoEuroCentAmount) throws VendingMachineException {
        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        moneyInventory.put(EuroMoneyType.ONE_CENT, oneCentAmount);
        moneyInventory.put(EuroMoneyType.TWO_CENT, twoCentAmount);
        moneyInventory.put(EuroMoneyType.FIVE_CENT, fiveCentAmount);
        moneyInventory.put(EuroMoneyType.TEN_CENT, tenCentAmount);
        moneyInventory.put(EuroMoneyType.TWENTY_CENT, twentyCentAmount);
        moneyInventory.put(EuroMoneyType.FIFTY_CENT, fiftyCentAmount);
        moneyInventory.put(EuroMoneyType.ONE_EURO, oneEuroCentAmount);
        moneyInventory.put(EuroMoneyType.TWO_EURO, twoEuroCentAmount);
    }

    @Override
    public void setProductInventory(int stockCocaCola, int stockFanta, int stockSprite, int stockWater, int stockTonicWater) throws VendingMachineException {

        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        if (stockFanta < 0 || stockSprite < 0 || stockWater < 0 || stockCocaCola < 0 || stockTonicWater < 0) {
            throw new VendingMachineException("Negative stock");
        }
        productInventory.put(ProductType.COCA_COLA, stockCocaCola);
        productInventory.put(ProductType.FANTA, stockFanta);
        productInventory.put(ProductType.SPRITE, stockSprite);
        productInventory.put(ProductType.WATER, stockWater);
        productInventory.put(ProductType.TONIC_WATER, stockTonicWater);

    }
}


