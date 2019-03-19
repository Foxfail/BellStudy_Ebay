import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class EbayTest {

    private static RemoteWebDriver driver;
    String login = "asdfa";
    String password = "asdfa";


    @BeforeClass
    public static void setUpBeforeClass() throws MalformedURLException {
        System.out.println("Начинаю тест Ebay");
        URL chromeDriverUrl = new URL("http://localhost:9515");

        //открываю браузер
        System.out.print("Загружаю драйвер...");
        driver = new RemoteWebDriver(chromeDriverUrl, new ChromeOptions());
        System.out.println("Загружено");

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @Test
    public void test(){
        System.out.println("1. Перейдите на сайт https://www.ebay.com/");
        driver.get("https://www.ebay.com/");

        System.out.println("2. Кликните на ссылку «Войдите»");
        driver.findElementByXPath("//*[@id=\"gh-ug\"]/a").click();


        if (!driver.findElementByXPath("//*[@id=\"userid\"]").isDisplayed()){
            // если поле для ввода емейла не отображено
            driver.findElementByXPath("//*[@id=\"PROCEED-TO-DEFAULT-BTN\"]").click();
        }
        driver.findElementByXPath("//*[@id=\"userid\"]").sendKeys(login);
        driver.findElementByXPath("//*[@id=\"pass\"]").sendKeys(password);
    }
}
