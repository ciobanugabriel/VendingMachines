package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public interface MachineInterface {

     void loadMoneyInMachine(EnumMap<EuroMoneyType, Integer> coins) throws VendingMachineException;

     EnumMap<EuroMoneyType, Integer> buyOneProduct(ProductType productType) throws VendingMachineException;

     EnumMap<EuroMoneyType, Integer> buyTwoProducts(ProductType firstProductType, ProductType secondProductType) throws VendingMachineException;

     EnumMap<EuroMoneyType,Integer> cancelTransaction() throws VendingMachineException;

     void setMoneyInventory(int type1, int type2, int type3, int type4, int type5, int type6, int type8,int type9) throws VendingMachineException;

     void setProductInventory(int code01Amount, int code02Amount, int code03Amount, int code04Amount, int code05Amount) throws VendingMachineException;


}
