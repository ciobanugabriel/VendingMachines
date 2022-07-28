package org.example;

import org.example.enums.EuroBanknoteType;
import org.example.enums.EuroCoinType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public interface MachineInterface {

     void loadCoinsInMachine(EnumMap<EuroCoinType, Integer> coins) throws VendingMachineException;

     void loadBanknotesInMachine(EnumMap<EuroBanknoteType, Integer> banknotes) throws VendingMachineException;

     EnumMap<EuroCoinType, Integer> buyOneProduct(ProductType productType) throws VendingMachineException;

     EnumMap<EuroCoinType, Integer> buyTwoProducts(ProductType firstProductType, ProductType secondProductType) throws VendingMachineException;

     EnumMap<EuroCoinType,Integer> cancelTransaction() throws VendingMachineException;

     boolean hasMoneyChange(int changeAmount);


     void setProductInventory(int code01Amount, int code02Amount, int code03Amount, int code04Amount, int code05Amount) throws VendingMachineException;

     //void setMoneyInventory(int )

}
