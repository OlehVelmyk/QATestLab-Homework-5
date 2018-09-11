package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        //throw new UnsupportedOperationException();

        driver.get(Properties.getBaseUrl());
        WebElement allGoodsLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("all-product-link")));
        allGoodsLink.click();

        //Click on  random product
        List<WebElement> list = driver.findElements(By.className("product-description"));
        if(list.size() != 0) {
            Random random = new Random();
            int i = random.nextInt(list.size()) + 1;
            list.get(i - 1).click();
        } else {

            System.out.println("Products list is empty");
        }
    }

    public void addProductToTrash(ProductData newProduct) {

        //Click on Trash button
        WebElement trashButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".btn.btn-primary.add-to-cart")));
        trashButton.click();

        //Click on go to the order button
        WebElement  successfullyPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myModalLabel")));
        WebElement goToTheOrderFormButton = driver.findElement(By.cssSelector("a.btn.btn-primary"));
        goToTheOrderFormButton.click();

        //Check quantity of the product
        String productQuantity = driver.findElement(By.name("product-quantity-spin")).getAttribute("value");
        Assert.assertEquals(Integer.parseInt(productQuantity), 1);

        //Check the product name
        String productName = driver.findElement(By.cssSelector(".product-line-info>a.label")).getText();
        Assert.assertTrue(newProduct.getName().equalsIgnoreCase(productName));

        //Check the product price
        String productPrice = driver.findElement(By.cssSelector(".col-xs-8>.product-line-info:nth-child(2)")).getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPrice), newProduct.getPrice());

    }
    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        //throw new UnsupportedOperationException();

        ProductData newProduct = null;
        float price;
        String productNameOnTheProductPage = driver.findElement(By.cssSelector(".h1")).getText();

        String productPriceOnTheProductPage = driver.findElement(By.cssSelector(".current-price>span")).getText();
        price = DataConverter.parsePriceValue(productPriceOnTheProductPage);

        List <WebElement> list = driver.findElements(By.cssSelector("div.tabs > ul > li > a"));

        if(list.size() == 2) {
            WebElement productDetailsLink = driver.findElement(By.cssSelector("div.tabs > ul > li:nth-child(2) > a"));
            productDetailsLink.click();
            String productQuantity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-quantities > span"))).getText();
            int parseQuantity = DataConverter.parseStockValue(productQuantity);

            newProduct = new ProductData(productNameOnTheProductPage, parseQuantity, price);
            System.out.println(newProduct.getName());
        } else {
            String productQuantity = driver.findElement(By.cssSelector(".product-quantities > span")).getText();
            int parseQuantity = DataConverter.parseStockValue(productQuantity);

            newProduct = new ProductData(productNameOnTheProductPage, parseQuantity, price);
            System.out.println(newProduct.getName());
        }
        return newProduct;
    }

    public void orderCreationOfTheProduct() {

        WebElement orderCreationButton = driver.findElement(By.cssSelector("a.btn.btn-primary"));
        orderCreationButton.click();

        WebElement firstNamefield = driver.findElement(By.name("firstname"));
        firstNamefield.sendKeys("User");

        WebElement lastNameField = driver.findElement(By.name("lastname"));
        lastNameField.sendKeys("Test");

        WebElement emailField = driver.findElement(By.name("email"));
        String email = "usertest" + System.currentTimeMillis() + "@gmail.com";
        emailField.sendKeys(email);

        WebElement continueButton = driver.findElement(By.cssSelector("#customer-form > footer > button"));
        continueButton.click();

        WebElement addressField = driver.findElement(By.name("address1"));
        addressField.sendKeys("street Shevchenko 5");

        WebElement postcodeField = driver.findElement(By.name("postcode"));
        postcodeField.sendKeys("34578");

        WebElement cityField = driver.findElement(By.name("city"));
        cityField.sendKeys("Kiev");

        WebElement newContinueButton = driver.findElement(By.name("confirm-addresses"));
        newContinueButton.click();

        WebElement nextButton = driver.findElement(By.name("confirmDeliveryOption"));
        nextButton.click();

        WebElement paymentMethod = driver.findElement(By.id("payment-option-1"));
        paymentMethod.click();

        WebElement conditionsCheckBox = driver.findElement(By.id("conditions_to_approve[terms-and-conditions]"));
        conditionsCheckBox.click();

        WebElement orderCreateButton = driver.findElement(By.cssSelector(".btn.btn-primary.center-block"));
        orderCreateButton.click();
    }

    public void checkTheOrderDetails(ProductData newProduct) {

        WebElement successfullMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".h1.card-title")));

        //Check quantity of the product
        String productQuantity = driver.findElement(By.className("col-xs-2")).getText();
        Assert.assertEquals(Integer.parseInt(productQuantity), 1);

        //Check the product name
        String productName = driver.findElement(By.cssSelector(".col-sm-4.col-xs-9.details>span")).getText();
        char[] array1 = new char[newProduct.getName().length()];
        char[] array = productName.toCharArray();
        for(int i = 0; i < newProduct.getName().length(); i++) {

            array1[i] = array[i];
        }
        String newProductName = new String(array1);
        Assert.assertTrue(newProduct.getName().equalsIgnoreCase(newProductName));

        //Check the product price
        String productPrice = driver.findElement(By.cssSelector(".col-xs-5.text-sm-right.text-xs-left")).getText();
        Assert.assertEquals(DataConverter.parsePriceValue(productPrice), newProduct.getPrice());
    }

    public void checkingTheProductQuantityUpdated(ProductData newProduct) {

        WebElement allGoodsLink = driver.findElement(By.className("all-product-link"));
        allGoodsLink.click();

        String input = newProduct.getName();//наша строчка
        String output = "";//все слова с заглавной буквы.
        String[] words = input.split(" ");//разделяем на массив из слов
        for(String word:words) {
            String first = word.substring(0, 1).toUpperCase();
            String all = word.substring(1).toLowerCase();
            String space = " ";
            output += first + all + space;
        }

        WebElement productDisplayed = driver.findElement(By.linkText(output.replaceAll("^\\s+|\\s+$", "")));
        Assert.assertTrue(productDisplayed.isDisplayed());
        productDisplayed.click();

        //Check the product quantity updated
        List <WebElement> list = driver.findElements(By.cssSelector("div.tabs > ul > li > a"));

        if(list.size() == 2) {
            WebElement productDetailsLink = driver.findElement(By.cssSelector("div.tabs > ul > li:nth-child(2) > a"));
            productDetailsLink.click();
            String productQuantity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-quantities > span"))).getText();
            int parseQuantity = DataConverter.parseStockValue(productQuantity);
            Assert.assertEquals(parseQuantity, newProduct.getQty() - 1);
        } else {
            String productQuantity = driver.findElement(By.cssSelector(".product-quantities > span")).getText();
            int parseQuantity = DataConverter.parseStockValue(productQuantity);
            Assert.assertEquals(parseQuantity, newProduct.getQty() - 1);
        }
    }
}
