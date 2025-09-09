package org.example.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;

public class RegisterPageTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Configura o Microsoft Edge WebDriver
        System.setProperty("webdriver.edge.driver", "C:\\Users\\igor.faggiani\\Downloads\\edgedriver_win64\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://demo.automationtesting.in/Register.html");
        // Remove iframes/anúncios
        ((JavascriptExecutor) driver).executeScript(
                "let iframes = document.getElementsByTagName('iframe');" +
                        "for (let iframe of iframes) { iframe.remove(); }"
        );
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void takeScreenshot(String testName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("screenshot_" + testName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillForm(String firstName, String lastName, String email, String phone, String gender, String country) {
        driver.findElement(By.cssSelector("input[ng-model='FirstName']")).sendKeys(firstName);
        driver.findElement(By.cssSelector("input[ng-model='LastName']")).sendKeys(lastName);
        driver.findElement(By.cssSelector("input[ng-model='EmailAdress']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[ng-model='Phone']")).sendKeys(phone);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Aumentado para 15s
        WebElement genderRadio = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='radio' and @value='" + gender + "']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", genderRadio);
        genderRadio.click();

        Select countryDropdown = new Select(driver.findElement(By.id("country")));
        countryDropdown.selectByVisibleText(country);

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submitbtn")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();
    }

    @Test
    public void testValidRegistration() {
        try {
            fillForm("John", "Doe", "john.doe@example.com", "1234567890", "Male", "India");
            Assert.assertEquals(driver.getCurrentUrl(), "https://demo.automationtesting.in/Register.html",
                    "Página redirecionou após registro válido");
        } catch (Exception e) {
            takeScreenshot("testValidRegistration");
            throw e;
        }
    }

    @Test
    public void testInvalidEmail() {
        try {
            fillForm("Bob", "Wilson", "invalid-email", "1234567890", "Male", "India");
            WebElement emailField = driver.findElement(By.cssSelector("input[ng-model='EmailAdress']"));
            String validationMessage = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].validationMessage;", emailField);
            Assert.assertTrue(validationMessage.contains("email"), "Erro de e-mail inválido não detectado");
        } catch (Exception e) {
            takeScreenshot("testInvalidEmail");
            throw e;
        }
    }

    @Test
    public void testEmptyRequiredFields() {
        try {
            fillForm("", "Taylor", "chris.taylor@example.com", "1234567890", "Male", "India");
            WebElement firstNameField = driver.findElement(By.cssSelector("input[ng-model='FirstName']"));
            String validationMessage = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].validationMessage;", firstNameField);
            Assert.assertTrue(validationMessage.contains("fill out"), "Erro de campo obrigatório não detectado");
        } catch (Exception e) {
            takeScreenshot("testEmptyRequiredFields");
            throw e;
        }
    }

    @Test
    public void testFemaleGenderRegistration() {
        try {
            fillForm("Emma", "Davis", "emma.davis@example.com", "1234567890", "FeMale", "India");
            Assert.assertEquals(driver.getCurrentUrl(), "https://demo.automationtesting.in/Register.html",
                    "Página redirecionou após registro com gênero Female");
        } catch (Exception e) {
            takeScreenshot("testFemaleGenderRegistration");
            throw e;
        }
    }

    @Test
    public void testInvalidPhoneFormat() {
        try {
            fillForm("Alice", "Brown", "alice.brown@example.com", "abc123", "Male", "India");
            WebElement phoneField = driver.findElement(By.cssSelector("input[ng-model='Phone']"));
            String validationMessage = (String) ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].validationMessage;", phoneField);
            Assert.assertTrue(validationMessage.contains("number"), "Erro de formato de telefone inválido não detectado");
        } catch (Exception e) {
            takeScreenshot("testInvalidPhoneFormat");
            throw e;
        }
    }

    @Test
    public void testDifferentCountrySelection() {
        try {
            fillForm("Mike", "Smith", "mike.smith@example.com", "9876543210", "Male", "United States of America");
            Assert.assertEquals(driver.getCurrentUrl(), "https://demo.automationtesting.in/Register.html",
                    "Página redirecionou após registro com país Estados Unidos");
        } catch (Exception e) {
            takeScreenshot("testDifferentCountrySelection");
            throw e;
        }
    }
}