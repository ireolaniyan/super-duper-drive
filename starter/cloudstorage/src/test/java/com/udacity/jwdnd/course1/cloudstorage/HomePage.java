package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    @FindBy(id = "logout-btn")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id = "new-note-btn")
    private WebElement newNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "save-changes")
    private WebElement saveNoteButton;

    @FindBy(id = "userTable")
    private WebElement notesTable;;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "new-credential-btn")
    private WebElement newCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "save-credential")
    private WebElement saveCredentialButton;

    @FindBy(id = "credentialTable")
    private WebElement credsTable;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void addNote(String noteTitleText, String noteDescriptionText, JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", notesTab);
        wait.withTimeout(Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(newNoteButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleField)).sendKeys(noteTitleText);
        noteDescriptionField.sendKeys(noteDescriptionText);
        saveNoteButton.click();
    }

    public boolean checkForNote(String noteTitleText, JavascriptExecutor jse) {
        jse.executeScript("arguments[0].click()", notesTab);
        List<WebElement> notesList = notesTable.findElements(By.tagName("th"));
        boolean created = false;
        for (int i=0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            if (element.getAttribute("innerHTML").equals(noteTitleText)) {
                created = true;
                break;
            }
        }

        return created;
    }

    public void updateNote(String noteTitleText, JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", notesTab);
        List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            editElement = element.findElement(By.name("edit-note-btn"));
            if (editElement != null){
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleField));
        noteTitleField.clear();
        noteTitleField.sendKeys(noteTitleText);
        saveNoteButton.click();
    }

    public boolean deleteNote(JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", notesTab);
        List<WebElement> notesList = notesTable.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        Boolean deleted = false;
        for (int i = 0; i < notesList.size(); i++) {
            WebElement element = notesList.get(i);
            deleteElement = element.findElement(By.name("delete-note-btn"));
            if (deleteElement != null){
                wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
                deleted = true;
                break;
            }
        }

        return deleted;
    }

    public void addCredentials(String credURL, String userName, String password, JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", credentialsTab);
        wait.withTimeout(Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(newCredentialButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(credentialUrlField)).sendKeys(credURL);
        credentialUsernameField.sendKeys(userName);
        credentialPasswordField.sendKeys(password);
        saveCredentialButton.click();
    }

    public boolean checkForCredentials(String userName, JavascriptExecutor jse) {
        jse.executeScript("arguments[0].click()", credentialsTab);
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        boolean created = false;
        for (int i=0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            if (element.getAttribute("innerHTML").equals(userName)) {
                created = true;
                break;
            }
        }

        return created;
    }

    public void updateCredentials(String newCredUsername, JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", credentialsTab);
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        WebElement editElement = null;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            editElement = element.findElement(By.name("edit-credential-btn"));
            if (editElement != null){
                break;
            }
        }
        wait.until(ExpectedConditions.elementToBeClickable(editElement)).click();
        wait.until(ExpectedConditions.elementToBeClickable(credentialUsernameField));
        credentialUsernameField.clear();
        credentialUsernameField.sendKeys(newCredUsername);
        saveCredentialButton.click();
    }

    public boolean deleteCredentials(JavascriptExecutor jse, WebDriverWait wait) {
        jse.executeScript("arguments[0].click()", credentialsTab);
        List<WebElement> credsList = credsTable.findElements(By.tagName("td"));
        WebElement deleteElement = null;
        Boolean deleted = false;
        for (int i = 0; i < credsList.size(); i++) {
            WebElement element = credsList.get(i);
            deleteElement = element.findElement(By.name("delete-credential-btn"));
            if (deleteElement != null) {
                wait.until(ExpectedConditions.elementToBeClickable(deleteElement)).click();
                deleted = true;
                break;
            }
        }

        return deleted;
    }
}
