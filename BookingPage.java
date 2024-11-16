package com.khushitours.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BookingPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public BookingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(driver, this);
    }

    // Web elements using @FindBy
    @FindBy(xpath = "//input[@value='Local Taxi']")
    private WebElement localTaxiOption;

    @FindBy(xpath = "//input[@value='Out Station Taxi']")
    private WebElement outStationTaxiOption;

    @FindBy(xpath = "//input[@value='Airport Services']")
    private WebElement airportServicesOption;

    @FindBy(id = "pick_up_city")
    private WebElement pickUpCity;

    @FindBy(id = "pick_up_location")
    private WebElement dropOffCity;

    @FindBy(id = "pick_date")
    private WebElement pickUpDate;

    @FindBy(id = "pick_time")
    private WebElement pickUpTime;

    @FindBy(id = "drop_date")
    private WebElement dropOffDate;

    @FindBy(id = "drop_time")
    private WebElement dropOffTime;

    @FindBy(xpath = "//button[text()='Search']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[contains(@class,'search-results')]")
    private WebElement searchResults;

    // Actions
    public void selectLocalTaxi() {
        wait.until(ExpectedConditions.elementToBeClickable(localTaxiOption)).click();
    }

    public void selectOutStationTaxi() {
        wait.until(ExpectedConditions.elementToBeClickable(outStationTaxiOption)).click();
    }

    public void selectAirportServices() {
        wait.until(ExpectedConditions.elementToBeClickable(airportServicesOption)).click();
    }

    public void setPickUpCity(String city) {
        new Select(pickUpCity).selectByVisibleText(city);
    }

    public void setDropOffCity(String city) {
        new Select(dropOffCity).selectByVisibleText(city);
    }

    public void setPickUpDate(String date) {
        pickUpDate.sendKeys(date);
    }

    public void setPickUpTime(String time) {
        pickUpTime.sendKeys(time);
    }

    public void setDropOffDate(String date) {
        dropOffDate.sendKeys(date);
    }

    public void setDropOffTime(String time) {
        dropOffTime.sendKeys(time);
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public boolean isResultsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(searchResults)).isDisplayed();
    }
}
