package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;
import java.util.TreeMap;
import java.util.logging.Level;

public class NonAlimentarVendingMachine extends VendingMachine {


    public NonAlimentarVendingMachine() {

        productInventory = new TreeMap<>();

        productInventory.put("A1", new MachineProduct(ProductType.MASK, PRODUCT_STOCK));
        productInventory.put("A2", new MachineProduct(ProductType.KENT, PRODUCT_STOCK));
        productInventory.put("A3", new MachineProduct(ProductType.MAGNET, PRODUCT_STOCK));

        productInventory.put("B1", new MachineProduct(ProductType.GUM, PRODUCT_STOCK));
        productInventory.put("B2", new MachineProduct(ProductType.KENT, PRODUCT_STOCK));
        productInventory.put("B3", new MachineProduct(ProductType.MASK, PRODUCT_STOCK));

        productInventory.put("C1", new MachineProduct(ProductType.KEY_HOLDER, PRODUCT_STOCK));
        productInventory.put("C2", new MachineProduct(ProductType.DUNHILL, PRODUCT_STOCK));
        productInventory.put("C3", new MachineProduct(ProductType.MASK, PRODUCT_STOCK));


        machineMoneyInventory = new EnumMap<>(EuroMoneyType.class);
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            if (!moneyTypeIsNotValid(moneyType)) {
                machineMoneyInventory.put(moneyType, MONEY_STOCK);
            }
        }
    }

    @Override
    public void setMoneyInventory(int hundredEuroAmount, int fiftyEuroAmount, int twentyEuroAmount, int tenEuroAmount, int fiveEuroAmount,
                                  int twoEuroCentAmount, int oneEuroCentAmount, int fiftyCentAmount) throws VendingMachineException {
        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before setting money inventory!",
                    new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        } else if (!user.isAdmin()) {
            logger.log(Level.INFO, "Only admin allowed!");
            throw new VendingMachineException("Only admin allowed!");
        }
        machineMoneyInventory.put(EuroMoneyType.HUNDRED_EURO, hundredEuroAmount);
        machineMoneyInventory.put(EuroMoneyType.FIFTY_EURO, fiftyEuroAmount);
        machineMoneyInventory.put(EuroMoneyType.TWENTY_EURO, twentyEuroAmount);
        machineMoneyInventory.put(EuroMoneyType.TEN_EURO, tenEuroAmount);
        machineMoneyInventory.put(EuroMoneyType.FIVE_EURO, fiveEuroAmount);
        machineMoneyInventory.put(EuroMoneyType.ONE_EURO, oneEuroCentAmount);
        machineMoneyInventory.put(EuroMoneyType.TWO_EURO, twoEuroCentAmount);
        machineMoneyInventory.put(EuroMoneyType.FIFTY_CENT, fiftyCentAmount);

        logger.log(Level.INFO, user.getName() + " is setting money inventory for " + getClass().getSimpleName());

        logged = false;
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
