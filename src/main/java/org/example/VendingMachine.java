package org.example;

import org.example.Interfaces.MachineInterface;
import org.example.Interfaces.UserInterface;
import org.example.enums.EuroMoneyType;
import org.example.exceptions.VendingMachineException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class VendingMachine implements MachineInterface {

    public static final int PRODUCT_STOCK = 5;
    public static final int MONEY_STOCK = 10;
    protected int totalEarning = 0;
    protected UserInterface user;
    protected Map<String, MachineProduct> productInventory;
    protected boolean logged = false;
    protected int valueOfMoneyLoaded;
    protected boolean moneyWasLoaded = false;
    protected EnumMap<EuroMoneyType, Integer> lastMoneyLoaded;
    protected EnumMap<EuroMoneyType, Integer> machineMoneyInventory;
    protected final Logger logger = Logger.getLogger(VendingMachine.class.getName());

    public final String createStatus() {
        StringBuilder message = new StringBuilder();

        message.append(getClass()).append(" STATUS : \n");
        message.append("\n\tProduct Status : \n");

        for (String slot : productInventory.keySet()) {
            message.append("\t\t").append(slot).append(" : ").append(productInventory.get(slot).getProductType())
                    .append(" : ").append(productInventory.get(slot).getProductType().getPrice()).append(" Cents. ---> ").append(
                            productInventory.get(slot).getStock()).append(" stock.\n");
        }

        message.append("\n\n\tMoney Status : \n");
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            if (!moneyTypeIsNotValid(moneyType)) {
                message.append("\t\t").append(moneyType).append(" ---> ").append(machineMoneyInventory.get(moneyType)).append(" stock.\n");
            }
        }
        message.append("\n").append("Total sales amount : ")
                .append(totalEarning / 100).append(" Euro and ").append(totalEarning % 100).append(" cents.");
        message.append("\n\n\n");
        return message.toString();
    }

    public static void writeStatus(String status) throws IOException {

        FileWriter output = new FileWriter("src/main/resources/status.txt");
        output.write(status);
        output.close();
    }

    public final void takeSupplementaryMoney() throws VendingMachineException, IOException {
        if (!logged) {
            logger.log(Level.SEVERE, "Please Log in", new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        } else if (!user.isAdmin()) {
            logger.log(Level.SEVERE, "Error", new VendingMachineException("Only admin allowed!"));
            throw new VendingMachineException("Only admin allowed!");
        }

        FileWriter output = new FileWriter("src/main/resources/adminStatus.txt");

        output.write("From " + getClass() + "\nAdmin takes : \n");
        for (EuroMoneyType moneyType : machineMoneyInventory.keySet()) {
            if (machineMoneyInventory.get(moneyType) > MONEY_STOCK) {

                int count = machineMoneyInventory.get(moneyType) - MONEY_STOCK;
                machineMoneyInventory.put(moneyType, MONEY_STOCK);
                output.write("\t" + count + " ->  " + moneyType + "\n");
                logger.log(Level.INFO, "From " + getClass() + "\nAdmin takes : \n" + "\t" + count + " ->  " + moneyType + "\n");
            }
        }
        output.close();
    }

    public final void logIn(UserInterface user) throws VendingMachineException {
        if (logged) {
            logger.log(Level.SEVERE, user.getName() + " is still logged!", new VendingMachineException("Another user is still logged!"));
            throw new VendingMachineException("Another user is still logged!");
        }
        logger.log(Level.INFO, user.getName() + " is logged on " + getClass().getSimpleName());
        this.user = user;
        logged = true;
    }

    protected abstract boolean moneyTypeIsNotValid(EuroMoneyType moneyType);

    public final void loadMoneyInMachine(EnumMap<EuroMoneyType, Integer> moneyLoadedInMachine) throws VendingMachineException {

        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before loading money.", new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        }

        logger.log(Level.INFO, user.getName() + " is loading money in " + getClass());

        lastMoneyLoaded = new EnumMap<>(moneyLoadedInMachine);

        lastMoneyLoaded.forEach((k, v) -> {

            if (moneyTypeIsNotValid(k)) {
                try {
                    throw new VendingMachineException("");
                } catch (VendingMachineException e) {
                    logger.log(Level.SEVERE, k + " is invalid for this machine",
                            new RuntimeException(k + " money is not valid for this machine!"));
                    throw new RuntimeException(k + " money is not valid for this machine!", e);
                }
            }
        });

        moneyLoadedInMachine.replaceAll((k, v) -> v = 0);

        lastMoneyLoaded.forEach((k, v) -> machineMoneyInventory.merge(k, v, Integer::sum));

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

    public final void buyOneProduct(String slot) throws VendingMachineException {

        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before buying something.", new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        }
        if (slot == null) {
            logger.log(Level.INFO, "Null given product");
            throw new VendingMachineException("Null given product!");
        } else if (productInventory.containsKey(slot)) {
            if (productInventory.get(slot).getStock() == 0) {
                logger.log(Level.INFO, slot + " has 0 stock!");
                throw new VendingMachineException(slot + " has 0 stock!");
            }
        } else {
            logger.log(Level.SEVERE, "Invalid slot!",
                    new VendingMachineException("Machine does not contain " + slot + " slot!"));
            throw new VendingMachineException("Machine does not contain " + slot + " slot!");
        }
        if (!moneyWasLoaded) {
            throw new VendingMachineException("The money has not been loaded!");
        }

        int remainingAmount = valueOfMoneyLoaded - productInventory.get(slot).getProductType().getPrice();

        if (remainingAmount < 0) {
            logger.log(Level.INFO, "Not enough money , need more " + (-remainingAmount) + " Cents");
            throw new VendingMachineException("Not enough money , need more " + (-remainingAmount) + " Cents");
        } else if (remainingAmount == 0) {
            finishTransaction();
            return;
        }
        if (hasMoneyChange(remainingAmount)) {

            logger.log(Level.INFO, user.getName() + " buy one " + productInventory.get(slot).getProductType() + " from " + getClass());
            productInventory.get(slot).setStock(productInventory.get(slot).getStock() - 1);
            totalEarning += productInventory.get(slot).getProductType().getPrice();
            loadMoneyInMachine(getChangeForUser(remainingAmount));
        } else {
            logger.log(Level.SEVERE, "The machine does not have change!",
                    new VendingMachineException("The machine does not have change!"));
            throw new VendingMachineException("The machine does not have change!");
        }
    }

    public final void setProductInventory(int stockA1, int stockA2, int stockA3, int stockB1, int stockB2, int stockB3,
                                          int stockC1, int stockC2, int stockC3) throws VendingMachineException {

        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before setting product inventory!",
                    new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        } else if (!user.isAdmin()) {
            logger.log(Level.SEVERE, "Only admin allowed!",
                    new VendingMachineException("Only admin allowed!"));
            throw new VendingMachineException("Only admin allowed!");
        }
        if (stockA1 < 0 || stockA2 < 0 || stockA3 < 0 ||
                stockB1 < 0 || stockB2 < 0 || stockB3 < 0 ||
                stockC1 < 0 || stockC2 < 0 || stockC3 < 0) {
            throw new VendingMachineException("Negative stock");
        }
        productInventory.get("A1").setStock(stockA1);
        productInventory.get("A2").setStock(stockA2);
        productInventory.get("A3").setStock(stockA3);

        productInventory.get("B1").setStock(stockB1);
        productInventory.get("B2").setStock(stockB2);
        productInventory.get("B3").setStock(stockB3);

        productInventory.get("C1").setStock(stockC1);
        productInventory.get("C2").setStock(stockC2);
        productInventory.get("C3").setStock(stockC3);

        logger.log(Level.INFO, user.getName() + " is setting product inventory for " + getClass().getSimpleName());

        logged = false;
    }

    protected final EnumMap<EuroMoneyType, Integer> getChangeForUser(int remainingAmount) {

        EnumMap<EuroMoneyType, Integer> amountOfMoneyChanged = new EnumMap<>(EuroMoneyType.class);

        for (EnumMap.Entry<EuroMoneyType, Integer> entry : machineMoneyInventory.entrySet()) {
            EuroMoneyType moneyType = entry.getKey();
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {

                if (remainingAmount - moneyType.getValue() >= 0) {

                    remainingAmount = remainingAmount - moneyType.getValue();
                    count++;
                    machineMoneyInventory.put(moneyType, entry.getValue() - 1);

                    if (amountOfMoneyChanged.isEmpty()) {
                        amountOfMoneyChanged.put(moneyType, 1);
                    } else if (amountOfMoneyChanged.containsKey(moneyType)) {
                        int newNumberOfCoins = 1 + amountOfMoneyChanged.get(moneyType);
                        amountOfMoneyChanged.put(moneyType, newNumberOfCoins);
                    } else {
                        amountOfMoneyChanged.put(moneyType, 1);
                    }
                } else {
                    break;
                }
            }
        }
        return amountOfMoneyChanged;
    }

    protected final boolean hasMoneyChange(int remainingAmount) {

        EnumMap<EuroMoneyType, Integer> copyOfMachineMoneyInventory = new EnumMap<>(machineMoneyInventory);

        for (EnumMap.Entry<EuroMoneyType, Integer> entry : copyOfMachineMoneyInventory.entrySet()) {

            EuroMoneyType moneyType = entry.getKey();

            if (moneyTypeIsNotValid(moneyType)) {
                continue;
            }
            byte count = 0;

            while (count < 5 && entry.getValue() > 0) {
                if (remainingAmount - moneyType.getValue() >= 0) {

                    remainingAmount = remainingAmount - moneyType.getValue();
                    count++;
                    copyOfMachineMoneyInventory.put(moneyType, entry.getValue() - 1);
                } else {
                    break;
                }
            }
        }
        return remainingAmount == 0;
    }

    public final EnumMap<EuroMoneyType, Integer> finishTransaction() throws VendingMachineException {

        if (!logged) {
            logger.log(Level.SEVERE, "Please log in before finish transaction!",
                    new VendingMachineException("Log in first!"));
            throw new VendingMachineException("Log in first!");
        }
        if (moneyWasLoaded) {
            lastMoneyLoaded.forEach((k, v) -> machineMoneyInventory.merge(k, v, (v1, v2) -> v1 - v2));
            moneyWasLoaded = false;
            logged = false;
            logger.log(Level.INFO, user.getName() + " has finished transaction.");
            return lastMoneyLoaded;
        } else {
            throw new VendingMachineException("No money loaded!");
        }
    }
}
