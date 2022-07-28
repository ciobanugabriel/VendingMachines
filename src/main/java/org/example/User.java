package org.example;

import org.example.enums.EuroBanknoteType;
import org.example.enums.EuroCoinType;
import org.example.exceptions.UserException;


import java.util.EnumMap;

public class User implements UserInterface {

    private boolean admin;
    @SuppressWarnings("unused")
    private String name;

    private EnumMap<EuroBanknoteType, Integer> banknotesInventory;

    private EnumMap<EuroCoinType, Integer> coinsInventory;

    @SuppressWarnings("unused")
    private User() {
    }

    public User(String name, boolean admin) {

        this.name = name;
        this.admin = admin;
        banknotesInventory = new EnumMap<>(EuroBanknoteType.class);
        coinsInventory = new EnumMap<>(EuroCoinType.class);
    }

    public boolean isAdmin() {
        return this.admin;
    }

    @Override
    public void setEuroBanknotesWallet(EuroBanknoteType banknoteType, int numberOfBanknotes) throws  UserException {
        if (numberOfBanknotes < 0) {
            throw new UserException("Negative number of banknotes!");

        }
        if(banknotesInventory.isEmpty()){
            banknotesInventory.put(banknoteType, numberOfBanknotes);
        }
        else if (banknotesInventory.containsKey(banknoteType)) {
            var newNumberOfBanknotes = numberOfBanknotes + banknotesInventory.get(banknoteType);
            banknotesInventory.put(banknoteType, newNumberOfBanknotes);
        }else{
            banknotesInventory.put(banknoteType, numberOfBanknotes);
        }

    }

    @SuppressWarnings("unused")
    public EuroBanknoteType getEuroCoinType(EuroBanknoteType euroCoinType, int numberOfEuroCoins){
        // sa returnez ce bani vreau sa incarc in aparat, iar in aparat sa creez un portofel care sa retina incarcatura unui user
        return null;
    }
    @Override
    public void setEuroCoinWallet(EuroCoinType coinType, int numberOfCoins) throws UserException {

        if (numberOfCoins < 0) {
            throw new UserException("Negative number of coins!");
        }
        if(coinsInventory.isEmpty()){
            coinsInventory.put(coinType, numberOfCoins);
        }
        else if (coinsInventory.containsKey(coinType)) {
            var newNumberOfCoins = numberOfCoins + coinsInventory.get(coinType);
            coinsInventory.put(coinType, newNumberOfCoins);
        }else{
            coinsInventory.put(coinType, numberOfCoins);
        }

    }

    @Override
    public EnumMap<EuroBanknoteType, Integer> getBanknotesInventory() {
        return banknotesInventory;
    }

    @Override
    public EnumMap<EuroCoinType, Integer> getCoinsInventory() {

            return coinsInventory;

    }

    @Override
    public void updateCoinsInventory(EnumMap<EuroCoinType, Integer> changeFromMachine){
        changeFromMachine.forEach((k, v) -> coinsInventory.merge(k, v, Integer::sum));
    }

    @Override
    public void updateEuroBanknotesInventory(EnumMap<EuroBanknoteType, Integer> changeFromMachine){
        changeFromMachine.forEach((k, v) -> banknotesInventory.merge(k, v, Integer::sum));
    }


}