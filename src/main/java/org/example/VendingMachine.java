package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;
import java.util.Map;

public abstract class VendingMachine implements MachineInterface {
    protected User user;
    protected int valueOfMoneyLoaded;
    protected boolean moneyWasLoaded = false;
    protected EnumMap<EuroMoneyType, Integer> lastMoneyLoaded;
    protected EnumMap<ProductType, Integer> productInventory;

    protected EnumMap<EuroMoneyType, Integer> moneyInventory;

    protected boolean admin;

    protected abstract boolean moneyTypeIsNotValid(EuroMoneyType moneyType);

    public void loadMoneyInMachine(EnumMap<EuroMoneyType, Integer> moneyLoadedInMachine) throws VendingMachineException {

        lastMoneyLoaded = new EnumMap<>(moneyLoadedInMachine);

        lastMoneyLoaded.forEach((k, v) -> {

            if (moneyTypeIsNotValid(k)) {
                try {
                    throw new VendingMachineException("");
                } catch (VendingMachineException e) {
                    throw new RuntimeException(k.toString() + " money is not valid for this machine!", e);
                }
            }
        });


        moneyLoadedInMachine.replaceAll((k, v) -> v = 0);

        lastMoneyLoaded.forEach((k, v) -> moneyInventory.merge(k, v, Integer::sum));

        if (!lastMoneyLoaded.isEmpty()) {
            moneyWasLoaded = true;
            valueOfMoneyLoaded = 0;
        } else {
            moneyWasLoaded = false;
            throw new VendingMachineException("Empty load!");
        }

        for (EnumMap.Entry<EuroMoneyType, Integer> entry : lastMoneyLoaded.entrySet()) {

            EuroMoneyType moneyType = entry.getKey();
            int valueOfMoney = moneyType.getValue() * entry.getValue();
            valueOfMoneyLoaded = valueOfMoneyLoaded + valueOfMoney;
        }
    }

    public EnumMap<EuroMoneyType, Integer> buyOneProduct(ProductType productType) throws VendingMachineException {

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

        int remainingAmount = valueOfMoneyLoaded - productType.getPrice();

        if (remainingAmount < 0) {
            throw new VendingMachineException("Not enough money , need more " + (-remainingAmount) + " Cents");
        }

        if (hasMoneyChange(remainingAmount)) {

            return getMoneyChange(remainingAmount);
        } else {
            throw new VendingMachineException("The machine does not have change!");
        }

    }

    public EnumMap<EuroMoneyType, Integer> buyTwoProducts(ProductType firstProductType, ProductType secondProductType) throws VendingMachineException {
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

        int remainingAmount = valueOfMoneyLoaded - (firstProductType.getPrice() + secondProductType.getPrice());

        if (remainingAmount < 0) {
            throw new VendingMachineException("Not enough money , need more " + (-remainingAmount) + " Cents");
        }

        if (hasMoneyChange(remainingAmount)) {

            return getMoneyChange(remainingAmount);
        } else {
            throw new VendingMachineException("The machine does not have change!");
        }
    }

    protected EnumMap<EuroMoneyType, Integer> getMoneyChange(int remainingAmount) {

        EnumMap<EuroMoneyType, Integer> amountMoneyChanged = new EnumMap<>(EuroMoneyType.class);

        for (EnumMap.Entry<EuroMoneyType, Integer> entry : moneyInventory.entrySet()) {

            EuroMoneyType moneyType = entry.getKey();
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {

                if (remainingAmount - moneyType.getValue() >= 0) {

                    remainingAmount = remainingAmount - moneyType.getValue();
                    count++;
                    moneyInventory.put(moneyType, entry.getValue() - 1);

                    if (amountMoneyChanged.isEmpty()) {
                        amountMoneyChanged.put(moneyType, 1);
                    } else if (amountMoneyChanged.containsKey(moneyType)) {
                        int newNumberOfCoins = 1 + amountMoneyChanged.get(moneyType);
                        amountMoneyChanged.put(moneyType, newNumberOfCoins);
                    }
                } else {
                    break;
                }
            }

        }
        return amountMoneyChanged;
    }

    protected boolean hasMoneyChange(int remainingAmount) {

        EnumMap<EuroMoneyType, Integer> copyOfMoneyInventory = new EnumMap<>(moneyInventory);


        for (EnumMap.Entry<EuroMoneyType, Integer> entry : copyOfMoneyInventory.entrySet()) {

            EuroMoneyType moneyType = entry.getKey();

            if (moneyTypeIsNotValid(moneyType)) {
                continue;
            }
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {
                if (remainingAmount - moneyType.getValue() >= 0) {

                    remainingAmount = remainingAmount - moneyType.getValue();
                    count++;
                    copyOfMoneyInventory.put(moneyType, entry.getValue() - 1);

                } else {
                    break;
                }

            }

        }
        return remainingAmount == 0;

    }

    public EnumMap<EuroMoneyType, Integer> cancelTransaction() throws VendingMachineException {

        if (moneyWasLoaded) {
            lastMoneyLoaded.forEach((k, v) -> moneyInventory.merge(k, v, (v1, v2) -> v1 - v2));
            moneyWasLoaded = false;
            return lastMoneyLoaded;
        } else {
            throw new VendingMachineException("No money loaded!");
        }
    }


}
