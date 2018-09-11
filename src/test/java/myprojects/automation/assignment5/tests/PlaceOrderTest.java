package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.GeneralActions;
import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.Properties;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version

        driver.get(Properties.getBaseUrl());
    }

    @Test
    public void createNewOrder() {
        // TODO implement order creation test

        // open random product
        actions.openRandomProduct();

        // save product parameters
        ProductData newProduct = actions.getOpenedProductInfo();

        // add product to Cart and validate product information in the Cart
        actions.addProductToTrash(newProduct);

        // proceed to order creation, fill required information
        actions.orderCreationOfTheProduct();

        // place new order and validate order summary
        actions.checkTheOrderDetails(newProduct);

        // check updated In Stock value
        actions.checkingTheProductQuantityUpdated(newProduct);
    }
}
