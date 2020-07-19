package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	private static String firstName = "checkFirstName";
	private static String lastName = "checkLastName";
	private static String userName = "root";
	private static String password = "password";
	private static String noteTitle = "Super test title";
	private static String noteDescription = "Super test description";
	private static String credURL = "example.com";

	public String baseURL;

	@LocalServerPort
	public int port;

	public static WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}

	@Test
	public void getUnauthorizedHomePage() {
		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getLoginPage() {
		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseURL + "/signup");
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void getUnauthorizedResultPage() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get(baseURL + "/home");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-button")));
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void newUserAccessTest() {
//		// signup
		driver.get(baseURL + "/signup");
		SignUpPage signupPage = new SignUpPage(driver);
		signupPage.signUp(firstName, lastName, userName, password);

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//logout
		driver.get(baseURL + "/home");
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		assertEquals("Login", driver.getTitle());

		//Try accessing homepage
		driver.get(baseURL + "/home");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void noteCreationTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//add note
		HomePage homePage = new HomePage(driver);
		homePage.addNote(noteTitle, noteDescription, jse, wait);
		assertEquals("Result", driver.getTitle());

		//check for note
		driver.get(baseURL + "/home");
		boolean created = homePage.checkForNote(noteTitle, jse);
		Assertions.assertTrue(created);

		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void noteUpdateTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		String newNoteTitle = "new note title";

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//update note
		HomePage homePage = new HomePage(driver);
		homePage.updateNote(newNoteTitle, jse, wait);
		assertEquals("Result", driver.getTitle());

		//check the updated note
		driver.get(baseURL + "/home");
		boolean edited = homePage.checkForNote(noteTitle, jse);
		Assertions.assertTrue(edited);

		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void noteDeletionTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//delete note
		HomePage homePage = new HomePage(driver);
		boolean deleted = homePage.deleteNote(jse, wait);
		Assertions.assertTrue(deleted);

		driver.get(baseURL + "/home");
		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void credentialCreationTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//add credential
		HomePage homePage = new HomePage(driver);
		homePage.addCredentials(credURL,userName, password, jse, wait);
		assertEquals("Result", driver.getTitle());

		//check for credential
		driver.get(baseURL + "/home");
		boolean created = homePage.checkForCredentials(userName, jse);
		Assertions.assertTrue(created);

		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void credentialUpdateTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;
		String newCredUsername = "newUser";

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//update credential
		HomePage homePage = new HomePage(driver);
		homePage.updateCredentials(newCredUsername, jse, wait);
		assertEquals("Result", driver.getTitle());

		//check the updated note
		driver.get(baseURL + "/home");
		boolean edited = homePage.checkForCredentials(newCredUsername, jse);
		Assertions.assertTrue(edited);

		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void credentialDeletionTest() {
		WebDriverWait wait = new WebDriverWait (driver, 30);
		JavascriptExecutor jse =(JavascriptExecutor) driver;

		//login
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(userName, password);
		assertEquals("Home", driver.getTitle());

		//delete credentials
		HomePage homePage = new HomePage(driver);
		boolean deleted = homePage.deleteCredentials(jse, wait);
		Assertions.assertTrue(deleted);

		driver.get(baseURL + "/home");
		homePage.logout();
		assertEquals("Login", driver.getTitle());
	}
}