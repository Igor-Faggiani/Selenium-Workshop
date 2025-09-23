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

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class AutomationRegisterTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Configura o WebDriver do Chrome automaticamente
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navega para o site de demonstração
        driver.get("http://demo.automationtesting.in/Register.html");
    }

    @AfterEach
    void tearDown() {
        // Fecha o navegador após cada teste
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Implementação CORRIGIDA do Caso de Teste CT01: Preencher formulário com dados válidos.
     */
    @Test
    void testePreencherFormularioComDadosValidos() {
        // Entradas de dados conforme o documento
        driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys("Fulano");
        driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys("da Silva");
        driver.findElement(By.tagName("textarea")).sendKeys("Rua Exemplo, 123");
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("fulano@example.com");
        driver.findElement(By.xpath("//input[@type='tel']")).sendKeys("47999999999");
        driver.findElement(By.xpath("//input[@value='Male']")).click(); // Gênero Masculino
        driver.findElement(By.id("checkbox1")).click(); // Hobby: Cricket

        // --- INÍCIO DA CORREÇÃO ---
        // Interage com o dropdown de país dinâmico
        // 1. Clica no container para abrir o campo de busca
        driver.findElement(By.xpath("//span[@role='combobox']")).click();
        // 2. Digita "India" no campo de busca que aparece
        driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("India");
        // 3. Clica no resultado "India" que aparece na lista
        driver.findElement(By.xpath("//li[text()='India']")).click();
        // --- FIM DA CORREÇÃO ---

        // Senhas
        driver.findElement(By.id("firstpassword")).sendKeys("Senha@123");
        driver.findElement(By.id("secondpassword")).sendKeys("Senha@123");

        // Saída Esperada: Verificar se o botão de envio está habilitado
        WebElement botaoSubmit = driver.findElement(By.id("submitbtn"));
        assertTrue(botaoSubmit.isEnabled(), "O botão de envio deveria estar habilitado.");
    }

    /**
     * Implementação do Caso de Teste CT05: Selecionar múltiplos hobbies.
     */
    @Test
    void testeSelecionarMultiplosHobbies() {
        // Entradas: Selecionar os hobbies Cricket, Movies e Hockey [cite: 134, 136]
        WebElement checkboxCricket = driver.findElement(By.id("checkbox1"));
        WebElement checkboxMovies = driver.findElement(By.id("checkbox2"));
        WebElement checkboxHockey = driver.findElement(By.id("checkbox3"));

        checkboxCricket.click();
        checkboxMovies.click();
        checkboxHockey.click();

        // Saída Esperada: Verificar se os checkboxes estão selecionados [cite: 135, 137, 139]
        assertTrue(checkboxCricket.isSelected(), "O hobby 'Cricket' deveria estar selecionado.");
        assertTrue(checkboxMovies.isSelected(), "O hobby 'Movies' deveria estar selecionado.");
        assertTrue(checkboxHockey.isSelected(), "O hobby 'Hockey' deveria estar selecionado.");
    }

    /**
     * Implementação do Caso de Teste CT06: Selecionar país no dropdown.
     */
    @Test
    void testeSelecionarPaisNoDropdown() {
        // Entrada: Selecionar "India" na lista de países [cite: 142]
        WebElement dropdownElement = driver.findElement(By.id("country"));
        Select dropdownPaises = new Select(dropdownElement);

        dropdownPaises.selectByValue("India");

        // Saída Esperada: O país selecionado no dropdown deve ser "India" [cite: 145]
        String paisSelecionado = dropdownPaises.getFirstSelectedOption().getText();
        assertEquals("India", paisSelecionado, "O país selecionado deveria ser 'India'.");
    }
}
