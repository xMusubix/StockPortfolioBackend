package own.watcharapon.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import own.watcharapon.payload.JittaPayload;
import own.watcharapon.payload.SymbolPayload;
import own.watcharapon.repository.WatchlistRepository;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessJittaUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessJittaUtils.class);
    private final String selector1 = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(1) > div:nth-child(1)";
    private final WatchlistRepository watchlistRepository;
    //    private final String xPath1 = "//*[@id=\"app\"]/div/div[3]/div/div/div/div[3]/div[1]/div/div/div[1]/div[1]/div";
    @Value("${chrome.url}")
    private String chromeUrl;

    public ProcessJittaUtils(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Async("jittaExecutor")
    public void getJittaScore(SymbolPayload symbolPayload) {
        JittaPayload jittaPayload = new JittaPayload();
//        try {
//            Elements select = Jsoup.connect("https://www.jitta.com/stock/" + symbolPayload.getMarket() + ":" + symbolPayload.getSymbol())
//                    .get()
//                    .select(selector1);
//
//            String[] splited = select.text().split("\\s+");
//            String[] jittaLine = new String[0];
//            if (splited.length == 11) {
//                jittaLine = splited[7].split("%");
//                jittaPayload.setLine(Float.parseFloat(jittaLine[0]));
//            } else if (splited.length == 12) {
//                jittaLine = splited[8].split("%");
//                jittaPayload.setLine(Float.parseFloat(jittaLine[0].replaceAll(",", "")));
//            }
//            jittaPayload.setScore(Float.parseFloat(splited[5].replaceAll("JITTA", "")));
//            jittaPayload.setState(jittaLine[1]);
//
//            watchlistRepository.updateJittaData(symbolPayload, jittaPayload);
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//        }

        try {
            ChromeOptions options = new ChromeOptions();
            options.setBinary("/opt/google/chrome/google-chrome");
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
            options.addArguments("disable-infobars"); // disabling infobars
            options.addArguments("--disable-extensions"); // disabling extensions
            options.addArguments("--disable-gpu"); // applicable to windows os only
            options.addArguments("window-size=1024,768"); // Bypass OS security model
            ChromeDriver driver = new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.ignoring(ElementNotInteractableException.class);
            driver.get("https://www.jitta.com/stock/" + symbolPayload.getMarket() + ":" + symbolPayload.getSymbol());

            Thread.sleep(5000);

            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("app")));

            String[] jittaElement = element.findElement(By.cssSelector(selector1)).getText().split("\\s+");

            jittaPayload.setScore(Float.parseFloat(jittaElement[5]));
            jittaPayload.setLine(Float.parseFloat(jittaElement[8].replace("%", "")));
            jittaPayload.setState(jittaElement[9]);

            driver.quit();

            watchlistRepository.updateJittaData(symbolPayload, jittaPayload);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
