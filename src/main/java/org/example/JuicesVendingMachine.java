package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;
import java.util.TreeMap;
import java.util.logging.Level;

public class JuicesVendingMachine extends VendingMachine {


    public JuicesVendingMachine() {

        productInventory = new TreeMap<>();

        productInventory.put("A1", new MachineProduct(ProductType.COCA_COLA, PRODUCT_STOCK));
        productInventory.put("A2", new MachineProduct(ProductType.FANTA, PRODUCT_STOCK));
        productInventory.put("A3", new MachineProduct(ProductType.SPRITE, PRODUCT_STOCK));

        productInventory.put("B1", new MachineProduct(ProductType.COCA_COLA, PRODUCT_STOCK));
        productInventory.put("B2", new MachineProduct(ProductType.FANTA, PRODUCT_STOCK));
        productInventory.put("B3", new MachineProduct(ProductType.TONIC_WATER, PRODUCT_STOCK));

        productInventory.put("C1", new MachineProduct(ProductType.SPRITE, PRODUCT_STOCK));
        productInventory.put("C2", new MachineProduct(ProductType.SPRITE, PRODUCT_STOCK));
        productInventory.put("C3", new MachineProduct(ProductType.SPRITE, PRODUCT_STOCK));

        machineMoneyInventory = new EnumMap<>(EuroMoneyType.class);
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            if (!moneyTypeIsNotValid(moneyType)) {
                machineMoneyInventory.put(moneyType, MONEY_STOCK);
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
        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before setting money inventory!",
                    new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");

        } else if (!user.isAdmin()) {
            logger.log(Level.INFO, "Only admin allowed!");
            throw new VendingMachineException("Only admin allowed!");
        }
        machineMoneyInventory.put(EuroMoneyType.ONE_CENT, oneCentAmount);
        machineMoneyInventory.put(EuroMoneyType.TWO_CENT, twoCentAmount);
        machineMoneyInventory.put(EuroMoneyType.FIVE_CENT, fiveCentAmount);
        machineMoneyInventory.put(EuroMoneyType.TEN_CENT, tenCentAmount);
        machineMoneyInventory.put(EuroMoneyType.TWENTY_CENT, twentyCentAmount);
        machineMoneyInventory.put(EuroMoneyType.FIFTY_CENT, fiftyCentAmount);
        machineMoneyInventory.put(EuroMoneyType.ONE_EURO, oneEuroCentAmount);
        machineMoneyInventory.put(EuroMoneyType.TWO_EURO, twoEuroCentAmount);

        logger.log(Level.INFO, user.getName() + " is setting money inventory for " + getClass().getSimpleName());

        logged = false;
    }

}


