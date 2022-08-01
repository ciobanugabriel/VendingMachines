package org.example;

import org.example.enums.EuroMoneyType;
import org.example.exceptions.UserException;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public interface UserInterface {


    void setMoneyInventory(EuroMoneyType moneyType, int numberOfMoney) throws UserException;
    EnumMap<EuroMoneyType, Integer> getMoneyForMachine();
    EnumMap<EuroMoneyType, Integer> getMoneyInventory();

    void setMoneyInventoryForMachine(EuroMoneyType moneyType, int numberOfMoney)throws UserException;
    void updateMoneyInventory(EnumMap<EuroMoneyType, Integer> changeFromMachine);


}
