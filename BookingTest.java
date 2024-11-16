package com.khushitours.tests;

import com.khushitours.pages.BookingPage;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BookingTest {
    private WebDriver driver;
    private BookingPage bookingPage;
    private Properties props;
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setup() {
        try {
            // Load properties
            props = new Properties();
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props.load(fis);

            // Set up WebDriver
            System.setProperty("webdriver.chrome.driver", props.getProperty("chromedriver.path"));
            WebDriverManager.chromedriver().setup(); // This will automatically download the correct driver
            driver = new ChromeDriver();
            driver.manage().window().maximize();

            // Initialize page and Extent Reports
            bookingPage = new BookingPage(driver);

            // Initialize ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extentReport_" + System.currentTimeMillis() + ".html");
            sparkReporter.config().setDocumentTitle("Khushi Tours Automation Report");
            sparkReporter.config().setReportName("Booking Test Report");
            sparkReporter.config().setTheme(Theme.STANDARD);

            // Initialize ExtentReports object and attach the sparkReporter
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Correct usage to get capabilities in Selenium 4.x
            Capabilities capabilities = ((ChromeDriver) driver).getCapabilities(); // Cast to ChromeDriver to get capabilities

            // Get the browser version correctly using the capabilities object
            String browserVersion = capabilities.getBrowserVersion(); // Get the browser version

            extent.setSystemInfo("OS", "Windows");
            extent.setSystemInfo("Tester", "Your Name");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Browser Version", browserVersion); // Use the correct browser version

        } catch (IOException e) {
            System.err.println("Error in setup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testLocalTaxiBooking() {
        test = extent.createTest("Local Taxi Booking Test");
        driver.get(props.getProperty("url"));

        try {
            bookingPage.selectLocalTaxi();
            bookingPage.setPickUpCity("Hubli");
            bookingPage.setDropOffCity("Dharwad");
            bookingPage.setPickUpDate("05/19/2024");
            bookingPage.setPickUpTime("10:30 AM");
            bookingPage.setDropOffDate("05/19/2024");
            bookingPage.setDropOffTime("8:30 PM");
            bookingPage.clickSearch();

            Assert.assertTrue(bookingPage.isResultsDisplayed(), "Taxi booking results for Hubli to Dharwad were not displayed!");
            test.pass("Test passed!");

            takeScreenshot("LocalTaxiResults");
        } catch (Exception e) {
            test.fail("Test failed due to: " + e.getMessage());
            Assert.fail("Test failed due to: " + e.getMessage());
        }
    }

    public void takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenshotDir = new File(props.getProperty("screenshot.path"));
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        FileUtils.copyFile(screenshot, new File(screenshotDir + "/" + fileName + ".png"));
    }

    @AfterMethod
    public void tearDownMethod(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                takeScreenshot("Failure_" + result.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();  // Close the browser only once after all tests
        extent.flush();  // Write the report to disk
    }
}
