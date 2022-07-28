package org.example;

import org.example.enums.EuroBanknoteType;
import org.example.enums.EuroCoinType;
import org.example.exceptions.UserException;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public interface UserInterface {


    void setEuroBanknotesWallet(EuroBanknoteType banknoteType, int numberOfBanknotes) throws VendingMachineException, UserException;

    void setEuroCoinWallet(EuroCoinType coinType, int numberOfCoins) throws UserException;

    EnumMap<EuroBanknoteType, Integer> getBanknotesInventory();

    EnumMap<EuroCoinType, Integer> getCoinsInventory();

    void updateCoinsInventory(EnumMap<EuroCoinType, Integer> changeFromMachine);

    void updateEuroBanknotesInventory(EnumMap<EuroBanknoteType, Integer> changeFromMachine);

}
