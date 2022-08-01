package org.example;

import org.example.enums.EuroMoneyType;
import org.example.exceptions.UserException;


import java.util.EnumMap;

public class User implements UserInterface {

    private final boolean admin;
    @SuppressWarnings("unused")
    private final String name;

    private EnumMap<EuroMoneyType, Integer> moneyInventory;

    private EnumMap<EuroMoneyType, Integer> moneyInventoryForMachine;


    public User(String name, boolean admin) {

        this.name = name;
        this.admin = admin;
        moneyInventory = new EnumMap<>(EuroMoneyType.class);
        moneyInventoryForMachine = new EnumMap<>(EuroMoneyType.class);
        for (EuroMoneyType moneyType : EuroMoneyType.values()) {
            moneyInventory.put(moneyType,5);
        }

    }

    public boolean isAdmin() {
        return this.admin;
    }

    @Override
    public void setMoneyInventory(EuroMoneyType moneyType, int numberOfMoney) throws UserException {
        if (numberOfMoney < 0) {
            throw new UserException("Negative number of money!");
        }
        if (moneyInventory.isEmpty()) {
            moneyInventory.put(moneyType, numberOfMoney);
        } else if (moneyInventory.containsKey(moneyType)) {
            int newNumberOfBanknotes = numberOfMoney + moneyInventory.get(moneyType);
            moneyInventory.put(moneyType, newNumberOfBanknotes);
        } else {
            moneyInventory.put(moneyType, numberOfMoney);
        }

    }


    @Override
    @SuppressWarnings("unused")
    public EnumMap<EuroMoneyType, Integer> getMoneyForMachine() {
        // sa returnez ce bani vreau sa incarc in aparat, iar in aparat sa creez un portofel care sa retina incarcatura unui user
        return moneyInventoryForMachine;
    }

    @Override
    public EnumMap<EuroMoneyType, Integer> getMoneyInventory() {
        return moneyInventory;
    }

    @Override
    public void setMoneyInventoryForMachine(EuroMoneyType moneyType, int numberOfMoney) throws UserException {
        if (numberOfMoney < 0) {
            throw new UserException("Negative number of money!");
        }
        if (!moneyInventory.containsKey(moneyType)) {
            throw new UserException(moneyType + " are not in inventory!");
        } else if (moneyInventory.get(moneyType) - numberOfMoney < 0) {
            throw new UserException("You don`t have enoungh " + moneyType + " in inventory!");
        }
        if (moneyInventoryForMachine.isEmpty()) {
            moneyInventoryForMachine.put(moneyType, numberOfMoney);
            moneyInventory.computeIfPresent(moneyType, (k, v) -> v - numberOfMoney);

        } else if (moneyInventoryForMachine.containsKey(moneyType)) {

            moneyInventoryForMachine.computeIfPresent(moneyType, (k, v) -> v + numberOfMoney);

            moneyInventory.computeIfPresent(moneyType, (k, v) -> v - numberOfMoney);

        } else {
            moneyInventoryForMachine.put(moneyType, numberOfMoney);
            moneyInventory.computeIfPresent(moneyType, (k, v) -> v - numberOfMoney);

        }
    }


    @Override
    public void updateMoneyInventory(EnumMap<EuroMoneyType, Integer> changeFromMachine) {
        changeFromMachine.forEach((k, v) -> moneyInventory.merge(k, v, Integer::sum));
    }


}