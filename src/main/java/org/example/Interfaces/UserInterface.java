package org.example.Interfaces;

import org.example.enums.EuroMoneyType;
import org.example.exceptions.UserException;

import java.io.IOException;
import java.util.EnumMap;

public interface UserInterface {
    boolean isAdmin();
    void checkstatus() throws IOException;
    String getName();
    void setMoneyInventory(EuroMoneyType moneyType, int numberOfMoney) throws UserException;
    EnumMap<EuroMoneyType, Integer> getMoneyForMachine();
    EnumMap<EuroMoneyType, Integer> getMoneyInventory();

    void setMoneyInventoryForMachine(EuroMoneyType moneyType, int numberOfMoney)throws UserException;
    void updateMoneyInventory(EnumMap<EuroMoneyType, Integer> changeFromMachine);


}
