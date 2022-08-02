package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public class NonAlimentarVendingMachine extends VendingMachine {
    public NonAlimentarVendingMachine(User user, boolean admin) {
        this.user = user;
        this.admin = admin;

        productInventory = new EnumMap<>(ProductType.class);
        productInventory.put(ProductType.MASK, 5);
        productInventory.put(ProductType.MAGNET, 5);
        productInventory.put(ProductType.KENT, 5);
        productInventory.put(ProductType.KEY_HOLDER, 5);
        productInventory.put(ProductType.DUNHILL, 5);

        moneyInventory = new EnumMap<>(EuroMoneyType.class);
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            if (!moneyTypeIsNotValid(moneyType)) {
                moneyInventory.put(moneyType, 10);
            }
        }
    }

    @Override
    public void setMoneyInventory(int hundredEuroAmount, int fiftyEuroAmount, int twentyEuroAmount, int tenEuroAmount, int fiveEuroAmount,
                                  int twoEuroCentAmount, int oneEuroCentAmount, int fiftyCentAmount) throws VendingMachineException {
        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        moneyInventory.put(EuroMoneyType.HUNDRED_EURO, hundredEuroAmount);
        moneyInventory.put(EuroMoneyType.FIFTY_EURO, fiftyEuroAmount);
        moneyInventory.put(EuroMoneyType.TWENTY_EURO, twentyEuroAmount);
        moneyInventory.put(EuroMoneyType.TEN_EURO, tenEuroAmount);
        moneyInventory.put(EuroMoneyType.FIVE_EURO, fiveEuroAmount);
        moneyInventory.put(EuroMoneyType.ONE_EURO, oneEuroCentAmount);
        moneyInventory.put(EuroMoneyType.TWO_EURO, twoEuroCentAmount);
        moneyInventory.put(EuroMoneyType.FIFTY_CENT, fiftyCentAmount);

    }


    @Override
    public void setProductInventory(int stockMask, int stockKent, int stockMagnet, int stockDunhill, int stockKeyHolder) throws VendingMachineException {

        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        if (stockMask < 0 || stockKent < 0 || stockMagnet < 0 || stockDunhill < 0 || stockKeyHolder < 0) {
            throw new VendingMachineException("Negative stock");
        }
        productInventory.put(ProductType.MASK, stockMask);
        productInventory.put(ProductType.KENT, stockKent);
        productInventory.put(ProductType.MAGNET, stockMagnet);
        productInventory.put(ProductType.DUNHILL, stockDunhill);
        productInventory.put(ProductType.KEY_HOLDER, stockKeyHolder);
    }

    @Override
    protected boolean moneyTypeIsNotValid(EuroMoneyType moneyType) {

        return switch (moneyType) {
            case HUNDRED_EURO -> false;
            case FIFTY_EURO -> false;
            case TWENTY_EURO -> false;
            case TEN_EURO -> false;
            case FIVE_EURO -> false;
            case FIFTY_CENT -> false;
            case ONE_EURO -> false;
            case TWO_EURO -> false;
            default -> true;
        };
    }
}
