package com.product.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.id("search_product");
    private By searchButton = By.id("submit_search");
    private By womenCategoryExpand = By.xpath("//*[@id=\"accordian\"]/div[1]/div[1]/h4/a");
    private By womenCategoryFirst = By.xpath("//*[@id=\"Women\"]/div/ul/li[1]/a");
    private By brandFilter = By.xpath("/html/body/section/div/div[2]/div[1]/div/div[2]/div/ul/li[2]/a");
    private By firstAddToCart = By.xpath("(//a[text()='Add to cart'])[1]");
    private By firstViewProduct = By.xpath("(//a[text()='View Product'])[1]");
    private By menCategoryExpand = By.xpath("//*[@id=\"accordian\"]/div[2]/div[1]/h4/a");
    private By menCategoryFirst = By.xpath("//*[@id=\"Men\"]/div/ul/li[1]/a");
    private By proceedToCheckout = By.xpath("//*[@id='do_action']/div[1]/div/div/a");
    private By viewCartInModal = By.xpath("//*[@id='cartModal']//u");

    // Constructor
    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void searchProduct(String productName) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(searchButton).click();
    }

    public void expandWomenCategory() {
        driver.findElement(womenCategoryExpand).click();
    }

    public void selectWomenSubCategory() {
        driver.findElement(womenCategoryFirst).click();
    }

    public void filterByBrand() {
        driver.findElement(brandFilter).click();
    }

    public void addFirstProductToCart() {
        driver.findElement(firstAddToCart).click();
    }

    public void viewFirstProduct() {
        driver.findElement(firstViewProduct).click();
    }

    public void expandMenCategory() {
        driver.findElement(menCategoryExpand).click();
    }

    public void selectMenSubCategory() {
        driver.findElement(menCategoryFirst).click();
    }

    public void clickViewCartInModal() {
        driver.findElement(viewCartInModal).click();
    }

    public void clickProceedToCheckout() {
        driver.findElement(proceedToCheckout).click();
    }
}
