package org.example;

import org.example.enums.EuroBanknoteType;
import org.example.enums.EuroCoinType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public class SnacksVendingMachine implements MachineInterface {
    @SuppressWarnings("unused")
    private User user;

    private int moneyLoaded;

    private boolean moneyWasLoaded = false;
    private EnumMap<EuroCoinType, Integer> lastMoneyLoaded;
    private EnumMap<ProductType, Integer> productInventory;

    private EnumMap<EuroCoinType, Integer> moneyInventory;

    private boolean admin;


    @SuppressWarnings("unused")
    private SnacksVendingMachine() {

    }

    public SnacksVendingMachine(User user, boolean admin) {
        this.user = user;
        this.admin = admin;

        productInventory = new EnumMap<>(ProductType.class);
        productInventory.put(ProductType.LAYS, 5);
        productInventory.put(ProductType.CHIO_CHIPS, 5);
        productInventory.put(ProductType.BAKE_ROLLS, 5);
        productInventory.put(ProductType.DORITOS, 5);
        productInventory.put(ProductType.M_AND_M, 5);

        moneyInventory = new EnumMap<>(EuroCoinType.class);
        moneyInventory.put(EuroCoinType.ONE_CENT, 10);
        moneyInventory.put(EuroCoinType.TWO_CENT, 10);
        moneyInventory.put(EuroCoinType.FIVE_CENT, 10);
        moneyInventory.put(EuroCoinType.TEN_CENT, 10);
        moneyInventory.put(EuroCoinType.TWENTY_CENT, 10);
        moneyInventory.put(EuroCoinType.FIFTY_CENT, 10);
        moneyInventory.put(EuroCoinType.ONE_EURO, 10);
        moneyInventory.put(EuroCoinType.TWO_EURO, 10);


    }


    @Override
    public void loadCoinsInMachine(EnumMap<EuroCoinType, Integer> coinsWalletLoaded) throws VendingMachineException {

        lastMoneyLoaded = new EnumMap<>(coinsWalletLoaded);

        coinsWalletLoaded.replaceAll((k, v) -> v = 0);

        lastMoneyLoaded.forEach((k, v) -> moneyInventory.merge(k, v, Integer::sum));

        if (!lastMoneyLoaded.isEmpty()) {
            moneyWasLoaded = true;
            moneyLoaded = 0;
        } else {
            moneyWasLoaded = false;
            throw new VendingMachineException("Empty load!");
        }

        for (var entry : lastMoneyLoaded.entrySet()) {

            var coinType = entry.getKey();
            var valueOfCoins = coinType.getValue() * entry.getValue();
            moneyLoaded = moneyLoaded + valueOfCoins;
        }


    }

    @Override
    public void loadBanknotesInMachine(EnumMap<EuroBanknoteType, Integer> banknotes) throws VendingMachineException {
        throw new VendingMachineException("Loading Banknotes is not available on this machine.");

    }

    @Override
    public EnumMap<EuroCoinType, Integer> buyOneProduct(ProductType productType) throws VendingMachineException {

        if (productType == null) {
            throw new VendingMachineException("Null given product!");
        } else if (productInventory.containsKey(productType)) {
            if (productInventory.get(productType) == 0) {
                throw new VendingMachineException(productType + " has 0 stock.");
            }
        } else {
            throw new VendingMachineException("Machine does not contain " + productType + " product!");
        }
        if (!moneyWasLoaded) {
            throw new VendingMachineException("The money has not been loaded!");
        }

        int remainingAmount = moneyLoaded - productType.getPrice();
        // moneyLoaded = 0;

        if (remainingAmount < 0) {
            throw new VendingMachineException("Not enough money , need more " + (-remainingAmount) + " Cents");
        }

        if (hasMoneyChange(remainingAmount)) {

            return getMoneyChange(remainingAmount);
        } else {
            throw new VendingMachineException("The machine does not have change!");
        }

    }

    private EnumMap<EuroCoinType, Integer> getMoneyChange(int remainingAmount) {

        EnumMap<EuroCoinType, Integer> amountMoneyChanged = new EnumMap<>(EuroCoinType.class);

        for (var entry : moneyInventory.entrySet()) {
            var coinType = entry.getKey();
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {

                if (remainingAmount - coinType.getValue() >= 0) {

                    remainingAmount = remainingAmount - coinType.getValue();
                    count++;
                    moneyInventory.put(coinType, entry.getValue() - 1);

                    if (amountMoneyChanged.isEmpty()) {
                        amountMoneyChanged.put(coinType, 1);
                    } else if (amountMoneyChanged.containsKey(coinType)) {
                        var newNumberOfCoins = 1 + amountMoneyChanged.get(coinType);
                        amountMoneyChanged.put(coinType, newNumberOfCoins);
                    }
                } else {
                    break;
                }
            }

        }
        return amountMoneyChanged;
    }

    @Override
    public EnumMap<EuroCoinType, Integer> buyTwoProducts(ProductType firstProductType, ProductType secondProductType) throws VendingMachineException {
        if (firstProductType == null && secondProductType != null) {
            return buyOneProduct(secondProductType);
        } else if (firstProductType != null && secondProductType == null) {
            return buyOneProduct(firstProductType);
        } else if (firstProductType == null) {
            throw new VendingMachineException("Null given products!");
        }

        if (productInventory.containsKey(firstProductType)) {
            if (productInventory.get(firstProductType) == 0) {
                throw new VendingMachineException(firstProductType + " has 0 stock.");
            }
        } else {
            throw new VendingMachineException("Machine does not contain " + firstProductType + " product!");
        }

        if (productInventory.containsKey(secondProductType)) {
            if (productInventory.get(secondProductType) == 0) {
                throw new VendingMachineException(secondProductType + " has 0 stock.");
            }
        } else {
            throw new VendingMachineException("Machine does not contain " + secondProductType + " product!");
        }

        if (!moneyWasLoaded) {
            throw new VendingMachineException("The money has not been loaded!");
        }

        int remainingAmount = moneyLoaded - (firstProductType.getPrice() + secondProductType.getPrice());

        if (remainingAmount < 0) {
            throw new VendingMachineException("Not enough money , need more " + (-remainingAmount) + " Cents");
        }

        if (hasMoneyChange(remainingAmount)) {

            return getMoneyChange(remainingAmount);
        } else {
            throw new VendingMachineException("The machine does not have change!");
        }
    }

    @Override
    public boolean hasMoneyChange(int remainingAmount) {

        var copyOfMoneyInventory = new EnumMap<>(moneyInventory);

        for (var entry : copyOfMoneyInventory.entrySet()) {
            var coinType = entry.getKey();
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {
                if (remainingAmount - coinType.getValue() >= 0) {

                    remainingAmount = remainingAmount - coinType.getValue();
                    count++;
                    copyOfMoneyInventory.put(coinType, entry.getValue() - 1);

                } else {
                    break;
                }

            }

        }
        return remainingAmount == 0;

    }

    @Override
    public EnumMap<EuroCoinType, Integer> cancelTransaction() throws VendingMachineException {

        if (moneyWasLoaded) {
            lastMoneyLoaded.forEach((k, v) -> moneyInventory.merge(k, v, (v1, v2) -> v1 - v2));
            moneyWasLoaded = false;
            return lastMoneyLoaded;
        } else {
            throw new VendingMachineException("No money loaded!");
        }
    }
    public void setMoneyInventory(int oneCentAmount, int twoCentAmount, int fiveCentAmount, int tenCentAmount,
                                  int twentyCentAmount, int fiftyCentAmount, int oneEuroCentAmount, int twoEuroCentAmount) {
        moneyInventory.put(EuroCoinType.ONE_CENT, oneCentAmount);
        moneyInventory.put(EuroCoinType.TWO_CENT, twoCentAmount);
        moneyInventory.put(EuroCoinType.FIVE_CENT, fiveCentAmount);
        moneyInventory.put(EuroCoinType.TEN_CENT, tenCentAmount);
        moneyInventory.put(EuroCoinType.TWENTY_CENT, twentyCentAmount);
        moneyInventory.put(EuroCoinType.FIFTY_CENT, fiftyCentAmount);
        moneyInventory.put(EuroCoinType.ONE_EURO, oneEuroCentAmount);
        moneyInventory.put(EuroCoinType.TWO_EURO, twoEuroCentAmount);
    }

    @Override
    public void setProductInventory(int stockLays, @SuppressWarnings("SpellCheckingInspection") int stockChioChips, int stockBakeRolls, int stockDoritos, int stockMAndM) throws VendingMachineException {

        if (!admin) {
            throw new VendingMachineException("Only admin allowed!");
        }
        if(stockChioChips < 0 || stockBakeRolls < 0 || stockDoritos < 0 || stockLays < 0 || stockMAndM < 0){
            throw new VendingMachineException("Negative stock");
        }
        productInventory.put(ProductType.LAYS, stockLays);
        productInventory.put(ProductType.CHIO_CHIPS, stockChioChips);
        productInventory.put(ProductType.BAKE_ROLLS, stockBakeRolls);
        productInventory.put(ProductType.DORITOS, stockDoritos);
        productInventory.put(ProductType.M_AND_M, stockMAndM);

    }
}
