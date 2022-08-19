package org.example;

import org.example.Interfaces.UserInterface;
import org.example.enums.EuroMoneyType;
import org.example.exceptions.UserException;
import org.example.exceptions.VendingMachineException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.example.User.USER_MONEY_STOCK;

public class VendingMachineTest {
    public final UserInterface user1 = new User("Alin", false);
    public final UserInterface user2 = new User("Cornel", false);
    public final UserInterface user3 = new User("Andrei", false);
    public final User admin = new User("Gabriel", true);
    public final SnacksVendingMachine snacksVendingMachine = new SnacksVendingMachine();
    public final JuicesVendingMachine juicesVendingMachine = new JuicesVendingMachine();
    public final NonAlimentarVendingMachine nonAlimentarVendingMachine = new NonAlimentarVendingMachine();

    @Test
    public void UltimateTest() throws VendingMachineException, UserException, IOException {

        juicesVendingMachine.logIn(admin);
        juicesVendingMachine.setProductInventory(2, 1, 0, 0,
                1, 0, 3, 0, 1);

        snacksVendingMachine.logIn(admin);
        snacksVendingMachine.setProductInventory(1, 2, 5, 0,
                2, 0, 1, 0, 0);

        nonAlimentarVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.setProductInventory(2, 0, 1, 5,
                0, 0, 0, 2, 0);

        ////-------------Action 1----------
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIVE_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_CENT, 1);

        juicesVendingMachine.logIn(user1);

        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());
        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("A3"));
        Assert.assertEquals(exception.getMessage(), "A3 has 0 stock!");

        juicesVendingMachine.buyOneProduct("B2");
        juicesVendingMachine.buyOneProduct("A1");

        user1.updateMoneyInventory(juicesVendingMachine.finishTransaction());

        EnumMap<EuroMoneyType, Integer> moneyInventory = user1.getMoneyInventory();
        Assert.assertEquals(USER_MONEY_STOCK, (int) moneyInventory.get(EuroMoneyType.TEN_CENT));
        Assert.assertEquals(USER_MONEY_STOCK, (int) moneyInventory.get(EuroMoneyType.FIVE_CENT));

        //--------------Action 2 -------------

        user2.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_EURO, 1);

        nonAlimentarVendingMachine.logIn(user2);
        nonAlimentarVendingMachine.loadMoneyInMachine(user2.getMoneyForMachine());
        nonAlimentarVendingMachine.buyOneProduct("A3");
        nonAlimentarVendingMachine.buyOneProduct("A1");
        exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("A3"));
        Assert.assertEquals(exception.getMessage(), "A3 has 0 stock!");

        exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("A2"));
        Assert.assertEquals(exception.getMessage(), "A2 has 0 stock!");

        user2.updateMoneyInventory(nonAlimentarVendingMachine.finishTransaction());

        moneyInventory = user2.getMoneyInventory();
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.TWO_EURO));
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.ONE_EURO));

        //-----------Action 3 -----------------------
        user2.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 3);
        snacksVendingMachine.logIn(user2);
        snacksVendingMachine.loadMoneyInMachine(user2.getMoneyForMachine());

        exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct("C3"));
        Assert.assertEquals(exception.getMessage(), "C3 has 0 stock!");
        snacksVendingMachine.buyOneProduct("A3");
        snacksVendingMachine.buyOneProduct("A3");
        snacksVendingMachine.buyOneProduct("A3");
        snacksVendingMachine.buyOneProduct("C1");

        user2.updateMoneyInventory(snacksVendingMachine.finishTransaction());

        moneyInventory = user2.getMoneyInventory();
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.FIFTY_CENT));
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.TWENTY_CENT));
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.FIVE_CENT));

        ///---------Action 4 ---------------

        user2.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 0);
        juicesVendingMachine.logIn(user2);
        juicesVendingMachine.loadMoneyInMachine(user2.getMoneyForMachine());

        exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("A1"));
        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 130 Cents");

        exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("C3"));
        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 125 Cents");
        juicesVendingMachine.finishTransaction();

        //---------Action 5 --------------------

        user3.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user3.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);
        user3.setMoneyInventoryForMachine(EuroMoneyType.FIVE_CENT, 1);

        juicesVendingMachine.logIn(user3);
        juicesVendingMachine.loadMoneyInMachine(user3.getMoneyForMachine());
        juicesVendingMachine.buyOneProduct("C1");

        //----------------Action 6----------------
        checkStatusForAllMachinesTest();

        user3.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 2);

        snacksVendingMachine.logIn(user3);
        snacksVendingMachine.loadMoneyInMachine(user3.getMoneyForMachine());
        snacksVendingMachine.buyOneProduct("A2");
        snacksVendingMachine.buyOneProduct("B2");

        user3.updateMoneyInventory(snacksVendingMachine.finishTransaction());
        moneyInventory = user3.getMoneyInventory();
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.FIFTY_CENT));
        Assert.assertEquals(USER_MONEY_STOCK + 1, (int) moneyInventory.get(EuroMoneyType.TEN_CENT));

        //-----------Action 7 ----------------------
        user3.checkstatus();
        user3.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        nonAlimentarVendingMachine.logIn(user3);
        nonAlimentarVendingMachine.loadMoneyInMachine(user3.getMoneyForMachine());

        exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("C2"));
        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 100 Cents");

        user3.updateMoneyInventory(nonAlimentarVendingMachine.finishTransaction());
        user3.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 2);
        nonAlimentarVendingMachine.logIn(user3);
        nonAlimentarVendingMachine.loadMoneyInMachine(user3.getMoneyForMachine());
        nonAlimentarVendingMachine.buyOneProduct("C2");

        user3.updateMoneyInventory(nonAlimentarVendingMachine.finishTransaction());
        moneyInventory = user3.getMoneyInventory();
        Assert.assertEquals(USER_MONEY_STOCK - 2, (int) moneyInventory.get(EuroMoneyType.ONE_EURO));

        juicesVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.logIn(admin);
        snacksVendingMachine.logIn(admin);

        juicesVendingMachine.setProductInventory(VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK);
        nonAlimentarVendingMachine.setProductInventory(VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK);
        snacksVendingMachine.setProductInventory(VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK,
                VendingMachine.PRODUCT_STOCK, VendingMachine.PRODUCT_STOCK);

        juicesVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.logIn(admin);
        snacksVendingMachine.logIn(admin);

        juicesVendingMachine.takeSupplementaryMoney();
        nonAlimentarVendingMachine.takeSupplementaryMoney();
        snacksVendingMachine.takeSupplementaryMoney();

        juicesVendingMachine.setMoneyInventory(VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK);
        nonAlimentarVendingMachine.setMoneyInventory(VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK);
        snacksVendingMachine.setMoneyInventory(VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK, VendingMachine.MONEY_STOCK,
                VendingMachine.MONEY_STOCK);
    }


    /////////////////User Test///////////////////////
    ////////////////____________//////////////////////

    @Test
    public void setMoneyInventoryForMachineTest() throws UserException {
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 2);

        EnumMap<EuroMoneyType, Integer> moneyForMachine = user1.getMoneyForMachine();

        Assert.assertEquals(3, (int) moneyForMachine.get(EuroMoneyType.FIFTY_EURO));
    }

    @Test
    public void setNegativeMoneyForMachineTest() {
        Exception exception = Assert.assertThrows(UserException.class,
                () -> user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, -5));
        Assert.assertEquals(exception.getMessage(), "Negative number of money!");
    }

    ////////////////////////SnacksMachine Test//////////////////////////////
    ///////////////////*************************//////////////////////////

    @Test
    public void checkStatusForAllMachinesTest() throws IOException {
        List<VendingMachine> list = new ArrayList<>();
        StringBuilder fullStatus = new StringBuilder();

        list.add(snacksVendingMachine);
        list.add(juicesVendingMachine);
        list.add(nonAlimentarVendingMachine);

        list.forEach(vendingMachine -> fullStatus.append(vendingMachine.createStatus()));
        VendingMachine.writeStatus(fullStatus.toString());
    }

    @Test
    public void finishTransactionSnacksMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 2);

        snacksVendingMachine.logIn(user1);

        snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = snacksVendingMachine.finishTransaction();
        user1.updateMoneyInventory(moneyForMachine);

        moneyForMachine = user1.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.ONE_CENT));
    }

    @Test
    public void takeSupplementaryMoneyFromSnacksMachineTest() throws VendingMachineException, IOException {
        snacksVendingMachine.logIn(admin);
        snacksVendingMachine.setMoneyInventory(12, 10, 9,
                0, 10, 12, 11, 15);
        snacksVendingMachine.logIn(admin);
        snacksVendingMachine.takeSupplementaryMoney();
    }

    @Test
    public void orderOneProductFromSnacksMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        snacksVendingMachine.logIn(user1);

        snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());
        snacksVendingMachine.buyOneProduct("A1");

        Assert.assertEquals(EnumMap.class, snacksVendingMachine.finishTransaction().getClass());
    }

    @Test
    public void orderOneProductFromSnacksMachineNotEnoughMoneyTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_CENT, 1);

        snacksVendingMachine.logIn(user1);

        snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct("C2"));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 5 Cents");
    }

    @Test
    public void orderOneNullProductFromSnacksMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        snacksVendingMachine.logIn(user1);

        snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");
    }

    @Test
    public void orderOneNonExistingProductFromSnacksMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        snacksVendingMachine.logIn(user1);

        snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct("C5"));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain C5 slot!");
    }

    @Test
    public void orderOneProductWithZeroStockFromSnacksMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        snacksVendingMachine.logIn(admin);

        snacksVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());

        snacksVendingMachine.setProductInventory(0, 0, 0, 0, 0, 0,
                0, 0, 0);

        snacksVendingMachine.logIn(admin);

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "A1 has 0 stock!");
    }

    @Test
    public void snacksMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 5);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        snacksVendingMachine.logIn(admin);

        snacksVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());
        snacksVendingMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);

        snacksVendingMachine.logIn(admin);

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> snacksVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");
    }


    @Test
    public void loadInvalidTypeOfMoneyInSnacksMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 1);
        snacksVendingMachine.logIn(user1);
        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> snacksVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "TEN_EURO money is not valid for this machine!");
    }

    //////////////////////JuicesMachine Test//////////////////////
    ///////////////@@@@@@@@@@@@@@@@@@@@@@@@@@@@@//////////////

    @Test
    public void finishTransactionJuicesMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 2);
        juicesVendingMachine.logIn(user1);

        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = juicesVendingMachine.finishTransaction();
        user1.updateMoneyInventory(moneyForMachine);

        moneyForMachine = user1.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.TEN_CENT));
    }

    @Test
    public void orderOneProductFromJuicesMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_CENT, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        juicesVendingMachine.logIn(user1);

        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());
        juicesVendingMachine.buyOneProduct("A1");

        Assert.assertEquals(EnumMap.class, juicesVendingMachine.finishTransaction().getClass());
    }

    @Test
    public void orderOneProductFromJuicesMachineNotEnoughMoneyTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        juicesVendingMachine.logIn(user1);
        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 10 Cents");
    }

    @Test
    public void orderOneNullProductFromJuicesMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        juicesVendingMachine.logIn(user1);
        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");
    }

    @Test
    public void orderOneNonExistingProductFromJuicesMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 2);

        juicesVendingMachine.logIn(user1);
        juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("V1"));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain V1 slot!");
    }

    @Test
    public void orderOneProductWithZeroStockFromJuicesMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        juicesVendingMachine.logIn(admin);
        juicesVendingMachine.setProductInventory(0, 0, 0, 0, 0, 0, 0, 0, 0);

        juicesVendingMachine.logIn(admin);
        juicesVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "A1 has 0 stock!");
    }

    @Test
    public void juicesMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 5);
        admin.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_CENT, 1);

        juicesVendingMachine.logIn(admin);
        juicesVendingMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);

        juicesVendingMachine.logIn(admin);
        juicesVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());


        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> juicesVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");
    }


    @Test
    public void loadInvalidTypeOfMoneyInJuicesMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        juicesVendingMachine.logIn(user1);
        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> juicesVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "FIFTY_EURO money is not valid for this machine!");
    }


    /////////////////////////NonAlimentVendingMachine TEST
    //////////////////&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//////////////////////////////////////


    @Test
    public void finishTransactionNonAlimentarMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 2);

        nonAlimentarVendingMachine.logIn(user1);
        nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        EnumMap<EuroMoneyType, Integer> moneyForMachine = nonAlimentarVendingMachine.finishTransaction();
        user1.updateMoneyInventory(moneyForMachine);

        moneyForMachine = user1.getMoneyInventory();
        Assert.assertEquals(5, (int) moneyForMachine.get(EuroMoneyType.FIFTY_EURO));
    }

    @Test
    public void orderOneProductFromNonAlimentarMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWO_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIVE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_EURO, 1);

        nonAlimentarVendingMachine.logIn(user1);
        nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());
        nonAlimentarVendingMachine.buyOneProduct("A1");

        Assert.assertEquals(EnumMap.class, nonAlimentarVendingMachine.finishTransaction().getClass());
    }

    @Test
    public void orderOneProductFromNonAlimentarMachineNotEnoughMoneyTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 1);

        nonAlimentarVendingMachine.logIn(user1);
        nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("A3"));

        Assert.assertEquals(exception.getMessage(), "Not enough money , need more 500 Cents");
    }

    @Test
    public void orderOneNullProductFromNonAlimentarMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.ONE_EURO, 1);
        user1.setMoneyInventoryForMachine(EuroMoneyType.TWENTY_EURO, 1);

        nonAlimentarVendingMachine.logIn(user1);
        nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct(null));

        Assert.assertEquals(exception.getMessage(), "Null given product!");
    }

    @Test
    public void orderOneNonExistingProductFromNonAlimentarMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.FIVE_EURO, 1);

        nonAlimentarVendingMachine.logIn(user1);
        nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("R1"));

        Assert.assertEquals(exception.getMessage(), "Machine does not contain R1 slot!");
    }

    @Test
    public void orderOneProductWithZeroStockFromNonAlimentarMachineTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.FIFTY_EURO, 1);

        nonAlimentarVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.setProductInventory(0, 0, 0, 0, 0, 0, 0, 0, 0);

        nonAlimentarVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "A1 has 0 stock!");
    }

    @Test
    public void nonAlimentarMachineDoesNotHaveChangeTest() throws Exception {

        admin.setMoneyInventoryForMachine(EuroMoneyType.TEN_EURO, 1);

        nonAlimentarVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.setMoneyInventory(0, 0, 0, 0,
                0, 0, 0, 0);

        nonAlimentarVendingMachine.logIn(admin);
        nonAlimentarVendingMachine.loadMoneyInMachine(admin.getMoneyForMachine());

        Exception exception = Assert.assertThrows(VendingMachineException.class,
                () -> nonAlimentarVendingMachine.buyOneProduct("A1"));

        Assert.assertEquals(exception.getMessage(), "The machine does not have change!");
    }


    @Test
    public void loadInvalidTypeOfMoneyInNonAlimentarMachineTest() throws Exception {
        user1.setMoneyInventoryForMachine(EuroMoneyType.TEN_CENT, 1);
        nonAlimentarVendingMachine.logIn(user1);

        Exception exception = Assert.assertThrows(RuntimeException.class,
                () -> nonAlimentarVendingMachine.loadMoneyInMachine(user1.getMoneyForMachine()));
        Assert.assertEquals(exception.getMessage(), "TEN_CENT money is not valid for this machine!");
    }
}