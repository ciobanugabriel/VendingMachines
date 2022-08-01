package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public class SnacksVendingMachine extends VendingMachine {


    public SnacksVendingMachine(User user, boolean admin) {
        this.user = user;
        this.admin = admin;

        productInventory = new EnumMap<>(ProductType.class);
        productInventory.put(ProductType.LAYS, 5);
        productInventory.put(ProductType.CHIO_CHIPS, 5);
        productInventory.put(ProductType.BAKE_ROLLS, 5);
        productInventory.put(ProductType.DORITOS, 5);
        productInventory.put(ProductType.M_AND_M, 5);

        moneyInventory = new EnumMap<>(EuroMoneyType.class);
        for( EuroMoneyType moneyType : EuroMoneyType.values()){
            if(!moneyTypeIsNotValid(moneyType)){
                moneyInventory.put(moneyType, 10);
            }
        }
    }


    @Override
    protected boolean moneyTypeIsNotValid(EuroMoneyType moneyType) {
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
    public void setProductInventory(int stockLays, @SuppressWarnings("SpellCheckingInspection") int stockChioChips, int stockBakeRolls, int stockDoritos, int stockMAndM) throws VendingMachineException {

        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        if (stockChioChips < 0 || stockBakeRolls < 0 || stockDoritos < 0 || stockLays < 0 || stockMAndM < 0) {
            throw new VendingMachineException("Negative stock");
        }
        productInventory.put(ProductType.LAYS, stockLays);
        productInventory.put(ProductType.CHIO_CHIPS, stockChioChips);
        productInventory.put(ProductType.BAKE_ROLLS, stockBakeRolls);
        productInventory.put(ProductType.DORITOS, stockDoritos);
        productInventory.put(ProductType.M_AND_M, stockMAndM);

    }
}
