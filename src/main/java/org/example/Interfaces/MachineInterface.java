package org.example.Interfaces;

import org.example.enums.EuroMoneyType;
import org.example.exceptions.VendingMachineException;

import java.util.EnumMap;

public interface MachineInterface {

     void loadMoneyInMachine(EnumMap<EuroMoneyType, Integer> coins) throws VendingMachineException;

     void buyOneProduct(String slot) throws VendingMachineException;

     EnumMap<EuroMoneyType,Integer> finishTransaction() throws VendingMachineException;

     void setMoneyInventory(int type1, int type2, int type3, int type4, int type5, int type6, int type8,int type9) throws VendingMachineException;

     void setProductInventory(int stockA1, int stockA2,int stockA3, int stockB1, int stockB2, int stockB3,int stockC1, int stockC2, int stockC3) throws VendingMachineException;


}
