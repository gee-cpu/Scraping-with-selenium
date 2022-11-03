package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {


    WebDriver driver;
    private int maxRecords = Integer.MAX_VALUE;
    private int start;

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
      //  ChromeOptions options = new ChromeOptions();
       // options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        // WebDriver driver = new ChromeDriver(options);

        String name = "George";

        Main main = new Main();
        main.start = 0;
        main.driver = main.getDriver();



        while (main.start < main.maxRecords)
            main.search(name);


    }

    private WebDriver getDriver() {
        WebDriver webDriver = new ChromeDriver();

       // final ChromeOptions chromeOptions = new ChromeOptions();
       // chromeOptions.addArguments("--headless");
        String baseURL = "https://exclusions.oig.hhs.gov/default.aspx";


        webDriver.manage().window().maximize();

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.get(baseURL);
        return webDriver;
    }


    private void search(String name) {
        String SsnXpath = "/html/body/div[2]/div[1]/div/div[3]/div[1]/div[2]/form/div[3]/div/table/tbody/tr/td/div/table/tbody/tr/td[8]/a";

        driver.findElement(By.xpath("//div[@id='content']/div[@class='search_block']/table[@class='leie_table search_name']/tbody/tr[2]/td[2]/div/input[@name='ctl00$cpExclusions$txtSPFirstName']")).sendKeys("Jane");
        driver.findElement(By.xpath("//div[@id='content']/div[@class='search_block']/table[@class='leie_table search_name']/tbody/tr[2]/td[1]/div/input[@name='ctl00$cpExclusions$txtSPLastName']")).sendKeys(name);
        driver.findElement(By.xpath("//div[@id='content']//input[@alt='Search']")).click();


        driver.findElement(By.xpath("\t//*[@id=\"ctl00_cpExclusions_gvEmployees\"]"));
        WebElement verifyTabs = driver.findElement(By.xpath(SsnXpath));

        List<WebElement> verifyTabsElements = verifyTabs.findElements(By.xpath(SsnXpath));
        int count = verifyTabsElements.size();
        maxRecords = count;
        for (int i = start; i < count; i++) {

            try {
                verifyTabs = driver.findElement(By.xpath(SsnXpath));
                WebElement element = verifyTabs.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div[1]/div[2]/form/div[3]/div/table/tbody/tr/td/div/table/tbody/tr[" + i + "]/td[8]/a"));
                element.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));

                try {
                    driver.findElement(By.xpath("//form[@id='aspnetForm']/div[@class='content']//div[@class='verify_ssn']//input[@name='ctl00$cpExclusions$txtSSN']"));
                    driver.findElement(By.xpath("//form[@id='aspnetForm']/div[@class='content']//div[@class='verify_ssn']//input[@name='ctl00$cpExclusions$txtSSN']")).sendKeys("123456789");
                    driver.findElement(By.xpath("//form[@id='aspnetForm']/div[@class='content']//div[@class='verify_ssn']//input[@name='ctl00$cpExclusions$ibtnVerify']")).click();
                    driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div[1]/div[2]/form/div[3]/div[2]/div[2]/div[2]/img"));



                    try {

                        List<WebElement> exclusionMatch = driver.findElements(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div[1]/div[2]/form/div[3]/div[2]/div[2]/div[2]/img"));

                        boolean isMatched;
                        for (WebElement elements : exclusionMatch) {


                            String src = elements.getAttribute("src");
                            if (src.contains("verify-no-match.png")){

                                isMatched = false;                            }
                            else {
                                isMatched = true;                            }

                        }
                        try {

                                WebElement table = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div[3]/div[1]/div[2]/form/div[3]/div[2]/table"));

                              List<WebElement> rowList = table.findElements(By.tagName("tr"));
                                for (WebElement row : rowList) {
                                        System.out.println(row.findElement(By.tagName("th")).getText() + " ---->  "+row.findElement(By.tagName("td")).getText());
                                }
                            }
                             catch (Exception e) {
                                e.printStackTrace();
                            }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (NoSuchElementException e) {
                    System.out.println("Element not found");
                    //start--;
                    break;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                driver.navigate().back();


            } catch (NoSuchElementException e) {
                System.out.println("Element not found");

            }

            start++;

        }


    }
}
