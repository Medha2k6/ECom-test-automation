package com.product.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    private By logo = By.xpath("//*[@id=\"header\"]/div/div/div/div[1]/div/a/img");
    private By productsLink = By.xpath("//*[@id=\"header\"]/div/div/div/div[2]/div/ul/li[2]/a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isLogoDisplayed() {
        return driver.findElement(logo).isDisplayed();
    }

    public void clickProducts() {
        driver.findElement(productsLink).click();
    }
}
