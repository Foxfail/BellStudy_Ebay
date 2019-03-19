import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.junit.Assert.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EbayTest {

    private static RemoteWebDriver driver;
    String login = "lookironic@gmail.com";
    String password = "AutomationT3sts";


    @BeforeClass
    public static void setUpBeforeClass() throws MalformedURLException {
        System.out.println("Начинаю тест Ebay");
        //открываю браузер

        try {
            System.out.print("Загружаю драйвер...");

//            закоменченные ниже строки работают если запускать драйвер вручную
//            URL chromeDriverUrl = new URL("http://localhost:9515");
//            driver = new RemoteWebDriver(chromeDriverUrl, new ChromeOptions());

            System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
            driver = new ChromeDriver();
            System.out.println("Загружено");
        } catch (Exception e) {
            System.out.println("Ошибка!!!");
            e.printStackTrace();
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
    }

    @Test
    public void test() {
        System.out.println("1. Перейдите на сайт https://www.ebay.com/");
        driver.get("https://www.ebay.com/");


        System.out.println("2. Кликните на ссылку «Войдите»");
        driver.findElementByXPath("//*[@id=\"gh-ug\"]/a").click();

        // если поле для ввода емейла не отображено
        if (!driver.findElementByXPath("//*[@id=\"userid\"]").isDisplayed()) {
            driver.findElementByXPath("//*[@id=\"PROCEED-TO-DEFAULT-BTN\"]").click();
        }


        System.out.println("3. Заполните логин и пароль и войдите в систему");
        driver.findElementByXPath("//*[@id=\"userid\"]").sendKeys(login);
        driver.findElementByXPath("//*[@id=\"pass\"]").sendKeys(password);

        driver.findElementByXPath("//*[@id=\"sgnBt\"]").click();

        // если отображается окно заполнения двухфакторной авторизации
        try {
            if (driver.findElementByXPath("//*[@id=\"secretQuesForm\"]/h1").isDisplayed()) {
                driver.findElementByXPath("//*[@id=\"rmdLtr\"]").click();
            }
        } catch (NoSuchElementException ignored) {
        }


        System.out.println("4. Перейдите на главную страницу сервиса");
        driver.findElementByXPath("//*[@id=\"gh-la\"]").click();


        System.out.println("5. Выполните поиск по товарам «samsung»");
        driver.findElementByXPath("//*[@id=\"gh-ac\"]").sendKeys("samsung");
        driver.findElementByXPath("//*[@id=\"gh-btn\"]").click();


        System.out.println("6. Убедитесь, что кол-во товаров совпадает с числом внизу «Кол-во товаров на странице»");
        // Ищем количество товаров актуально отбраженных на странице
        List<WebElement> itemsList = driver.findElementsByXPath("//*[@id=\"srp-river-results\"]/ul/li");
        int actualItemsOnPage = itemsList.size();

        // Находим число внизу «Кол-во товаров на странице»
        String s_ItemsOnPageSetting = driver.findElementByXPath("//*[@id=\"srp-river-results-SEARCH_PAGINATION_MODEL_V2-w1\"]/button/div").getText();
        int i_ItemsOnPageSetting = Integer.valueOf(s_ItemsOnPageSetting);

        // Проверяем равны ли они друг другу
        Assert.assertEquals(i_ItemsOnPageSetting, actualItemsOnPage);


        System.out.println("7. Убедиться, что в каждом из выведенных товаров присутствует слово «samsung»");
        // Используем itemsList из предыдущего шага
        for (WebElement we : itemsList){
            // Извлекаем название товара
            WebElement we_caption = we.findElement(By.xpath(".//div/div[2]/a/h3"));
            String s_caption = we_caption.getText();
            // Проходим по всем названиям в поисках названия samsung
            Assert.assertTrue(s_caption.toLowerCase().contains("samsung"));
        }


        System.out.println("8. Выйдите из под своей учетной записи");
        driver.findElementByXPath("//*[@id=\"gh-ug\"]").click();
        driver.findElementByXPath("//*[@id=\"gh-uo\"]/a").click();


        System.out.println("9. Проверьте, что вы успешно вышли из под учетной записи.");
        String exit_caption = driver.findElementByXPath("//*[@id=\"AreaTitle\"]/div/table/tbody/tr/td/div/div[2]/div/h1/span").getText();
        Assert.assertTrue(exit_caption.contains("Выход успешно выполнен"));
    }
}
