package org.example.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterSystemTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.edgedriver().setup(); // usa EdgeDriver
        driver = new EdgeDriver(); // inicializa Edge
        driver.manage().window().maximize();
        driver.get("https://demo.automationtesting.in/Register.html");
    }


    // ================================
    // TESTES DE SISTEMA
    // ================================

    @Test
    @DisplayName("CT01 - Preencher formulário com dados válidos")
    void testFormValid() {
        driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys("Fulano");
        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys("da Silva");
        driver.findElement(By.xpath("//textarea[@ng-model='Adress']")).sendKeys("Rua Exemplo, 123");
        driver.findElement(By.xpath("//input[@ng-model='EmailAdress']")).sendKeys("fulano@example.com");
        driver.findElement(By.xpath("//input[@ng-model='Phone']")).sendKeys("47999999999");
        driver.findElement(By.xpath("//input[@value='Male']")).click();
        driver.findElement(By.xpath("//input[@value='Cricket']")).click();

        Select country = new Select(driver.findElement(By.id("countries")));
        country.selectByVisibleText("India");

        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        driver.findElement(By.id("secondpassword")).sendKeys("Senha@123");

        boolean isEnabled = driver.findElement(By.id("submitbtn")).isEnabled();
        assertTrue(isEnabled, "Botão de envio deveria estar habilitado.");
    }

    @Test
    @DisplayName("CT02 - Campos obrigatórios não preenchidos")
    void testFormEmpty() {
        boolean isEnabled = driver.findElement(By.id("submitbtn")).isEnabled();
        assertFalse(isEnabled, "Botão não deveria estar habilitado sem dados.");
    }

    @Test
    @DisplayName("CT03 - E-mail inválido")
    void testInvalidEmail() {
        driver.findElement(By.xpath("//input[@ng-model='EmailAdress']")).sendKeys("email-invalido");
        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        driver.findElement(By.id("secondpassword")).sendKeys("Senha@123");

        WebElement emailField = driver.findElement(By.xpath("//input[@ng-model='EmailAdress']"));
        String value = emailField.getAttribute("value");
        assertTrue(value.contains("invalido"), "Campo deveria conter e-mail digitado inválido.");
    }

    @Test
    @DisplayName("CT04 - Senhas diferentes")
    void testDifferentPasswords() {
        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        driver.findElement(By.id("secondpassword")).sendKeys("SenhaErrada");

        String senha1 = driver.findElement(By.id("firstpassword")).getAttribute("value");
        String senha2 = driver.findElement(By.id("secondpassword")).getAttribute("value");

        assertNotEquals(senha1, senha2, "As senhas não deveriam ser iguais.");
    }

    @Test
    @DisplayName("CT05 - Selecionar múltiplos hobbies")
    void testMultipleHobbies() {
        driver.findElement(By.xpath("//input[@value='Cricket']")).click();
        driver.findElement(By.xpath("//input[@value='Movies']")).click();
        driver.findElement(By.xpath("//input[@value='Hockey']")).click();

        boolean cricket = driver.findElement(By.xpath("//input[@value='Cricket']")).isSelected();
        boolean movies = driver.findElement(By.xpath("//input[@value='Movies']")).isSelected();
        boolean hockey = driver.findElement(By.xpath("//input[@value='Hockey']")).isSelected();

        assertTrue(cricket && movies && hockey, "Todos os hobbies deveriam estar selecionados.");
    }

    @Test
    @DisplayName("CT06 - Selecionar país no dropdown")
    void testSelectCountry() {
        Select country = new Select(driver.findElement(By.id("countries")));
        country.selectByVisibleText("India");

        WebElement selected = country.getFirstSelectedOption();
        assertEquals("India", selected.getText(), "O país selecionado deveria ser India.");
    }

    // ================================
    // FINALIZAÇÃO
    // ================================


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
