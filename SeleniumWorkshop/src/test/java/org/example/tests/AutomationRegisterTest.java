package org.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class AutomationRegisterTest {

    private WebDriver driver;
    private final String PAGE_URL = "https://demo.automationtesting.in/Register.html";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get(PAGE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * CT01 - Preencher formulário com dados válidos[cite: 105].
     */
    @Test
    void testePreencherFormularioComDadosValidos() {
        driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys("Fulano");
        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys("da Silva");
        driver.findElement(By.tagName("textarea")).sendKeys("Rua Exemplo, 123");
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("fulano@example.com");
        driver.findElement(By.xpath("//input[@type='tel']")).sendKeys("47999999999");
        driver.findElement(By.xpath("//input[@value='Male']")).click(); // Gênero Masculino
        driver.findElement(By.id("checkbox1")).click();

        driver.findElement(By.xpath("//span[@role='combobox']")).click();
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("India");
        driver.findElement(By.xpath("//li[text()='India']")).click();

        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        driver.findElement(By.id("secondpassword")).sendKeys("Senha@123");

        WebElement botaoSubmit = driver.findElement(By.id("submitbtn"));
        assertTrue(botaoSubmit.isEnabled(), "O botão de envio deveria estar habilitado.");
    }

    /**
     * CT02 - Campos obrigatórios não preenchidos.
     */
    @Test
    void testeCamposObrigatoriosNaoPreenchidos() {
        WebElement botaoSubmit = driver.findElement(By.id("submitbtn"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoSubmit);

        assertEquals(PAGE_URL, driver.getCurrentUrl(), "A página não deveria ter sido submetida.");

        WebElement firstNameField = driver.findElement(By.xpath("//input[@placeholder='First Name']"));
        String validationMessage = firstNameField.getAttribute("validationMessage");
        assertFalse(validationMessage.isEmpty(), "Deveria haver uma mensagem de validação para o campo obrigatório.");
    }


    /**
     * CT03 - E-mail inválido.
     */
    @Test
    void testeEmailInvalido() {
        WebElement emailField = driver.findElement(By.xpath("//input[@type='email']"));
        emailField.sendKeys("email-invalido");

        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).click();

        String validationMessage = emailField.getAttribute("validationMessage");
        assertTrue(validationMessage.contains("@"), "A mensagem de validação deveria indicar a falta do '@'.");
    }

    /**
     * CT04 - Senhas diferentes.
     */
    @Test
    void testeSenhasDiferentes() {
        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        WebElement secondPasswordField = driver.findElement(By.id("secondpassword"));
        secondPasswordField.sendKeys("SenhaErrada");

        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).click();

        WebElement botaoSubmit = driver.findElement(By.id("submitbtn"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoSubmit);

        assertEquals(PAGE_URL, driver.getCurrentUrl(), "O formulário não deveria ser enviado com senhas diferentes.");
    }

    /**
     * CT05 - Selecionar múltiplos hobbies[cite: 131].
     */
    @Test
    void testeSelecionarMultiplosHobbies() {
        WebElement checkboxCricket = driver.findElement(By.id("checkbox1"));
        WebElement checkboxMovies = driver.findElement(By.id("checkbox2"));
        WebElement checkboxHockey = driver.findElement(By.id("checkbox3"));

        checkboxCricket.click();
        checkboxMovies.click();
        checkboxHockey.click();

        assertTrue(checkboxCricket.isSelected(), "O hobby 'Cricket' deveria estar selecionado.");
        assertTrue(checkboxMovies.isSelected(), "O hobby 'Movies' deveria estar selecionado.");
        assertTrue(checkboxHockey.isSelected(), "O hobby 'Hockey' deveria estar selecionado.");
    }

    /**
     * CT06 - Selecionar país no dropdown[cite: 140].
     */
    @Test
    void testeSelecionarPaisNoDropdown() {
        WebElement dropdownElement = driver.findElement(By.id("country"));
        Select dropdownPaises = new Select(dropdownElement);

        dropdownPaises.selectByValue("India");

        String paisSelecionado = dropdownPaises.getFirstSelectedOption().getText();
        assertEquals("India", paisSelecionado, "O país selecionado deveria ser 'India'.");
    }
}