package org.example;

import org.example.enums.EuroBanknoteType;
import org.example.enums.EuroCoinType;
import org.example.enums.ProductType;
import org.example.exceptions.UserException;
import org.example.exceptions.VendingMachineException;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;

public class VendingMachineTest {
    public final User user = new User("Alin", false);
    public final User admin = new User("Gabriel", true);

    public final SnacksVendingMachine adminUserMachine = new SnacksVendingMachine(admin, admin.isAdmin());
    public final SnacksVendingMachine userMachine = new SnacksVendingMachine(user, user.isAdmin());


    @Test
    public void setBanknotesInventoryTest() throws  UserException {
        user.setEuroBanknotesWallet(EuroBanknoteType.FIFTY_EURO, 1);
        user.setEuroBanknotesWallet(EuroBanknoteType.FIFTY_EURO, 2);

        var banknotesInventory = user.getBanknotesInventory();

        
        Assert.assertEquals(3,(int)banknotesInventory.get(EuroBanknoteType.FIFTY_EURO));
    }

    @Test
    public void cancelTransactionTest() throws Exception {
        user.setEuroCoinWallet(EuroCoinType.ONE_CENT, 1);
        user.setEuroCoinWallet(EuroCoinType.ONE_CENT, 2);

        userMachine.loadCoinsInMachine(user.getCoinsInventory());

        var coinsInventory = userMachine.cancelTransaction();
        user.updateCoinsInventory(coinsInventory);


        coinsInventory = user.getCoinsInventory();
        Assert.assertEquals(3,(int)coinsInventory.get(EuroCoinType.ONE_CENT));

    }

    @Test
    public void orderOneProductTest() throws Exception {
        user.setEuroCoinWallet(EuroCoinType.ONE_CENT, 1);
        user.setEuroCoinWallet(EuroCoinType.TWO_EURO, 1);
        user.setEuroCoinWallet(EuroCoinType.ONE_EURO, 1);
        user.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        userMachine.loadCoinsInMachine(user.getCoinsInventory());

        Assert.assertEquals(EnumMap.class,userMachine.buyOneProduct(ProductType.DORITOS).getClass());

    }

    @Test
    public void orderOneProductNotEnoughMoneyTest() throws Exception {
        user.setEuroCoinWallet(EuroCoinType.ONE_EURO, 1);
        user.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        userMachine.loadCoinsInMachine(user.getCoinsInventory());

        

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userMachine.buyOneProduct(ProductType.DORITOS));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 157 Cents");

    }

    @Test
    public void orderOneNullProductTest() throws Exception {
        user.setEuroCoinWallet(EuroCoinType.ONE_EURO, 1);
        user.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        userMachine.loadCoinsInMachine(user.getCoinsInventory());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");

    }

    @Test
    public void orderOneNonExistingProductTest() throws Exception {
        user.setEuroCoinWallet(EuroCoinType.ONE_EURO, 1);
        user.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        userMachine.loadCoinsInMachine(user.getCoinsInventory());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userMachine.buyOneProduct(ProductType.COCA_COLA));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain COCA_COLA product!");

    }

    @Test
    public void orderOneProductWithZeroStockTest() throws Exception {

        admin.setEuroCoinWallet(EuroCoinType.ONE_EURO, 1);
        admin.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        adminUserMachine.loadCoinsInMachine(admin.getCoinsInventory());
        adminUserMachine.setProductInventory( 0,0,0,0,0);


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserMachine.buyOneProduct(ProductType.LAYS));

        Assert.assertEquals(exception.getMessage(), "LAYS has 0 stock.");

    }
    @Test
    public void machineDoesNotHaveChangeTest() throws Exception {

        admin.setEuroCoinWallet(EuroCoinType.ONE_EURO, 5);
        admin.setEuroCoinWallet(EuroCoinType.TWENTY_CENT, 1);

        adminUserMachine.loadCoinsInMachine(admin.getCoinsInventory());
        adminUserMachine.setMoneyInventory(0,0,0,0,
                0,0,0,0);


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserMachine.buyOneProduct(ProductType.LAYS));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");

    }
    @Test
    public void loadNegativeNumberOfCoinsTest(){
        Exception exception = Assert.assertThrows(UserException.class,
                () -> user.setEuroCoinWallet(EuroCoinType.ONE_EURO,-5));
        Assert.assertEquals(exception.getMessage(),"Negative number of coins!");
    }

    @Test
    public void loadNegativeNumberOfBanknotesTest(){
        Exception exception = Assert.assertThrows(UserException.class,
                () -> user.setEuroBanknotesWallet(EuroBanknoteType.FIFTY_EURO,-5));
        Assert.assertEquals(exception.getMessage(),"Negative number of banknotes!");
    }


}