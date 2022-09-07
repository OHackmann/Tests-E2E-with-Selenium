package br.com.alura.leilao.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.junit.Assert;

public class LoginTest {
	
	private static final String URL_LOGIN = "http://localhost:8080/login";
	private WebDriver browser;

	@BeforeAll
	public static void beforeAll() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
	}
	
	@BeforeEach
	public void beforeEach() {
		this.browser = new ChromeDriver();
		this.browser.navigate().to(URL_LOGIN);
	}
	
	@AfterEach
	public void AfterEach() {
		this.browser.quit();
	}

	@Test
	public void deveriaEfetuarLoginComDadosValidos() {
		browser.findElement(By.id("username")).sendKeys("fulano");
		browser.findElement(By.id("password")).sendKeys("pass");
		browser.findElement(By.id("login-form")).submit();
		
		Assert.assertFalse(browser.getCurrentUrl().equals(URL_LOGIN));
		Assert.assertEquals("fulano", browser.findElement(By.id("usuario-logado")).getText());
	}
	
	@Test
	public void naoDeveriaLogarComDadosInvalidos() {
		browser.findElement(By.id("username")).sendKeys("invalido");
		browser.findElement(By.id("password")).sendKeys("123123");
		browser.findElement(By.id("login-form")).submit();
		
		Assert.assertTrue(browser.getCurrentUrl().equals("http://localhost:8080/login?error"));
		Assert.assertTrue("fulano", browser.getPageSource().contains("Usuário e senha inválidos."));
		Assert.assertThrows(NoSuchElementException.class, () -> browser.findElement(By.id("usuario-logado")));
	}
	
	@Test
	public void naoDeveriaAcessarPaginaRestritaSemEstarLogado() {
		this.browser.navigate().to("http://localhost:8080/login/leiloes/2");
		
		Assert.assertTrue(browser.getCurrentUrl().equals(URL_LOGIN));
		Assert.assertFalse(browser.getPageSource().contains("Dados do Leilão"));
	}
	
}
