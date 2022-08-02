package org.example;

import org.example.enums.EuroMoneyType;
import org.example.enums.ProductType;
import org.example.exceptions.UserException;
import org.example.exceptions.VendingMachineException;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;

public class VendingMachineTest {
    public final User user = new User("Alin", false);
    public final User admin = new User("Gabriel", true);

    public final SnacksVendingMachine adminUserSnacksMachine = new SnacksVendingMachine(admin, admin.isAdmin());
    public final SnacksVendingMachine userSnacksMachine = new SnacksVendingMachine(user, user.isAdmin());

    /////////////////User Test///////////////////////
    ////////////////____________//////////////////////


    @Test
    public void setMoneyInventoryForMachineTest() throws UserException {
        user.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 2);

        EnumMap<EuroMoneyType, Integer> moneyForMachine = user.getMoneyForMachine();

        Assert.assertEquals(3, (int) moneyForMachine.get(EuroMoneyType.FIFTY_EURO));
    }

    @Test
    public void setNegativeMoneyForMachineTest() {
        Exception exception = Assert.assertThrows(UserException.class,
                () -> user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, -5));
        Assert.assertEquals(exception.getMessage(), "Negative number of money!");
    }


    ////////////////////////SnacksMachine Test//////////////////////////////
    ///////////////////*************************//////////////////////////


    @Test
    public void cancelTransactionSnacksMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 2);

        userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = userSnacksMachine.cancelTransaction();
        user.updateMoneyInventory(moneyForMachine);


        moneyForMachine = user.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.ONE_CENT));

    }

    @Test
    public void orderOneProductFromSnacksMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine());

        Assert.assertEquals(EnumMap.class, userSnacksMachine.buyOneProduct(ProductType.DORITOS).getClass());

    }

    @Test
    public void orderOneProductFromSnacksMachineNotEnoughMoneyTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userSnacksMachine.buyOneProduct(ProductType.DORITOS));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 157 Cents");

    }

    @Test
    public void orderOneNullProductFromSnacksMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userSnacksMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");

    }

    @Test
    public void orderOneNonExistingProductFromSnacksMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userSnacksMachine.buyOneProduct(ProductType.COCA_COLA));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain COCA_COLA product!");

    }

    @Test
    public void orderOneProductWithZeroStockFromSnacksMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        adminUserSnacksMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserSnacksMachine.setProductInventory(0, 0, 0, 0, 0);

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserSnacksMachine.buyOneProduct(ProductType.LAYS));

        Assert.assertEquals(exception.getMessage(), "LAYS has 0 stock.");

    }

    @Test
    public void snacksMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 5);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        adminUserSnacksMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserSnacksMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserSnacksMachine.buyOneProduct(ProductType.LAYS));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");

    }


    @Test
    public void loadInvalidTypeOfMoneyInSnacksMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 1);
        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> userSnacksMachine.loadMoneyInMachine(user.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "TEN_EURO money is not valid for this machine!");
    }

    //////////////////////JuicesMachine Test//////////////////////
    ///////////////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//////////////


    JuicesVendingMachine userJuicesMachine = new JuicesVendingMachine(user, user.isAdmin());
    JuicesVendingMachine adminUserJuicesMachine = new JuicesVendingMachine(admin, admin.isAdmin());

    @Test
    public void cancelTransactionJuicesMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 2);

        userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = userJuicesMachine.cancelTransaction();
        user.updateMoneyInventory(moneyForMachine);


        moneyForMachine = user.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.TEN_CENT));

    }

    @Test
    public void orderOneProductFromJuicesMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine());

        Assert.assertEquals(EnumMap.class, userJuicesMachine.buyOneProduct(ProductType.COCA_COLA).getClass());

    }

    @Test
    public void orderOneProductFromJuicesMachineNotEnoughMoneyTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userJuicesMachine.buyOneProduct(ProductType.TONIC_WATER));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 47 Cents");

    }

    @Test
    public void orderOneNullProductFromJuicesMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userJuicesMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");

    }

    @Test
    public void orderOneNonExistingProductFromJuicesMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userJuicesMachine.buyOneProduct(ProductType.DORITOS));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain DORITOS product!");

    }

    @Test
    public void orderOneProductWithZeroStockFromJuicesMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        adminUserJuicesMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserJuicesMachine.setProductInventory(0, 0, 0, 0, 0);

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserJuicesMachine.buyOneProduct(ProductType.WATER));

        Assert.assertEquals(exception.getMessage(), "WATER has 0 stock.");

    }

    @Test
    public void juicesMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 5);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        adminUserJuicesMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserJuicesMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserJuicesMachine.buyOneProduct(ProductType.TONIC_WATER));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");

    }


    @Test
    public void loadInvalidTypeOfMoneyInJuicesMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> userJuicesMachine.loadMoneyInMachine(user.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "FIFTY_EURO money is not valid for this machine!");
    }


    /////////////////////////NonAlimentVendingMachine TEST
    //////////////////&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//////////////////////////////////////


    NonAlimentarVendingMachine userNonAlimentarMachine = new NonAlimentarVendingMachine(user, user.isAdmin());
    NonAlimentarVendingMachine adminUserNonAlimentarMachine = new NonAlimentarVendingMachine(admin, admin.isAdmin());

    @Test
    public void cancelTransactionNonAlimentarMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 2);

        userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = userNonAlimentarMachine.cancelTransaction();
        user.updateMoneyInventory(moneyForMachine);


        moneyForMachine = user.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.FIFTY_EURO));

    }

    @Test
    public void orderOneProductFromNonAlimentarMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.FIVE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_EURO, 1);

        userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine());

        Assert.assertEquals(EnumMap.class, userNonAlimentarMachine.buyOneProduct(ProductType.MAGNET).getClass());

    }

    @Test
    public void orderOneProductFromNonAlimentarMachineNotEnoughMoneyTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 1);

        userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userNonAlimentarMachine.buyOneProduct(ProductType.KENT));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 550 Cents");

    }

    @Test
    public void orderOneNullProductFromNonAlimentarMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_EURO, 1);

        userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userNonAlimentarMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");

    }

    @Test
    public void orderOneNonExistingProductFromNonAlimentarMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user.setMoneyInventoryForMachine(EuroMoneyType.FIVE_EURO, 1);

        userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> userNonAlimentarMachine.buyOneProduct(ProductType.DORITOS));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain DORITOS product!");

    }

    @Test
    public void orderOneProductWithZeroStockFromNonAlimentarMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        admin.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);

        adminUserNonAlimentarMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserNonAlimentarMachine.setProductInventory(0, 0, 0, 0, 0);

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserNonAlimentarMachine.buyOneProduct(ProductType.DUNHILL));

        Assert.assertEquals(exception.getMessage(), "DUNHILL has 0 stock.");

    }

    @Test
    public void nonAlimentarMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 3);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 3);

        adminUserNonAlimentarMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        adminUserNonAlimentarMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> adminUserNonAlimentarMachine.buyOneProduct(ProductType.KEY_HOLDER));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");

    }


    @Test
    public void loadInvalidTypeOfMoneyInNonAlimentarMachineTest() throws Exception {
        user.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 1);
        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> userNonAlimentarMachine.loadMoneyInMachine(user.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "TEN_CENT money is not valid for this machine!");
    }

}