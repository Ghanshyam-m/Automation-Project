package ddf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MultipleDDF {

    public static void main(String[] args) throws IOException {

        // Launch browser
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Valid credentials
        String validun = "Admin";
        String validpw = "admin123";

        // File path
        File file = new File("C:\\Users\\HP\\eclipse-workspace1\\Automation\\testdata\\Multiple.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;

        // Skip header
        br.readLine();

        // Read CSV
        while ((line = br.readLine()) != null) {

            // Skip empty lines
            if (line.trim().isEmpty()) continue;

            String[] data = line.split(",");

            // Validate row
            if (data.length < 2) {
                System.out.println("Invalid row: " + line);
                continue;
            }

            String username = data[0].trim();
            String password = data[1].trim();

            System.out.println(username + " " + password);

            // Open login page (fresh each iteration)
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Wait for login page
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

            // Enter credentials
            driver.findElement(By.name("username")).clear();
            driver.findElement(By.name("username")).sendKeys(username);

            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys(password);

            driver.findElement(By.className("oxd-button")).click();

            try {
                // Wait for dashboard (successful login)
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h6[text()='Dashboard']")));

                // Role validation
                if (username.equalsIgnoreCase(validun) && password.equalsIgnoreCase(validpw)) {
                    System.out.println("user logged is an Admin role");
                } else if (username.equalsIgnoreCase("Prajkta")) {
                    System.out.println("user logged is an ESS role");
                }

                // Logout
                WebElement dropdown = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//img[@class='oxd-userdropdown-img']")));
                dropdown.click();

                wait.until(ExpectedConditions.elementToBeClickable(
                        By.linkText("Logout"))).click();

                // Wait for login page again
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

            } catch (Exception e) {
                // Login failed
                System.out.println("invalid user");
            }
        }

        // Close resources
        br.close();
        driver.quit();
    }
}