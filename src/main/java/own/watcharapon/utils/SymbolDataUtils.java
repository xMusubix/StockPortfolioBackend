package own.watcharapon.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import own.watcharapon.payload.SymbolDataPayload;

import java.net.URI;
import java.net.URL;
import java.time.Duration;

@Component
public class SymbolDataUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SymbolDataUtils.class);
    //    private static final String INDUSTRY_SELECTOR = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(2) > div:nth-child(1) > div > div > div:nth-child(1) > div.JittaRanking__FlexItem-sc-1k2m2aq-1.bVCoEZ > a > div";
//    private static final String SECTOR_SELECTOR = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(2) > div:nth-child(1) > div > div > div:nth-child(2) > div.JittaRanking__FlexItem-sc-1k2m2aq-1.bVCoEZ > a > div";
    private static final String INDUSTRY_SELECTOR = "//*[@id=\"app\"]/div[1]/div[3]/div[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[7]/div[1]/div[1]/div[1]/div[3]/div[1]/a/div[1]";
    private static final String SECTOR_SELECTOR = "//*[@id=\"app\"]/div[1]/div[3]/div[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[7]/div[1]/div[1]/div[1]/div[4]/div[1]/a/div[1]";
    @Value("${chrome.url}")
    private String chromeUrl;

    public SymbolDataPayload checkSymbol(String symbol) {
        try {
            String[] markets = {"NASDAQ", "NYSE"};
            for (String market : markets) {
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

                try {
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                    wait.ignoring(ElementNotInteractableException.class);
                    driver.get("https://www.jitta.com/stock/" + market + ":" + symbol);

                    Thread.sleep(3000);

                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("app")));

                    String sectorString = element.findElement(By.xpath(SECTOR_SELECTOR)).getText();

                    if (!sectorString.startsWith("HTTP") && !sectorString.isEmpty()) {
                        LOG.info("{} Sector : {}", symbol, sectorString);
                    }

                    String industryString = element.findElement(By.xpath(INDUSTRY_SELECTOR)).getText();

                    if (!industryString.startsWith("HTTP") && !industryString.isEmpty()) {
                        LOG.info("{} Industry : {}", symbol, industryString);
                    }

                    driver.quit();

                    if (StringUtils.hasText(sectorString) && StringUtils.hasText(industryString)) {
                        return new SymbolDataPayload(sectorString, industryString, market);
                    }

                } catch (Exception e) {
                    LOG.warn("{}:{} | Error : {}", market, symbol, e.getMessage());
                    driver.quit();
                }
//                Document document = Jsoup.connect("https://www.jitta.com/stock/" + market + ":" + symbol)
//                        .get();
//
//                Elements sectorSelect = document.selectXpath(SECTOR_SELECTOR);
//
//                String sectorString = sectorSelect.text();
//                if (!sectorString.startsWith("HTTP") && !sectorString.isEmpty()) {
//                    LOG.info("{} Sector : {}", symbol, sectorString);
//                }
//
//                Elements industrySelect = document.selectXpath(INDUSTRY_SELECTOR);
//
//                String industryString = industrySelect.text();
//                if (!industryString.startsWith("HTTP") && !industryString.isEmpty()) {
//                    LOG.info("{} Industry : {}", symbol, industryString);
//                }
//
//                if (StringUtils.hasText(sectorString) && StringUtils.hasText(industryString)) {
//                    return new SymbolDataPayload(sectorString, industryString, market);
//                }
            }
            return null;
        } catch (Exception ex) {
            LOG.error("Error fetching data for symbol {} : {}", symbol, ex.getMessage());
            if (ex.getClass().equals(HttpStatusException.class)) {
                HttpStatusException exception = (HttpStatusException) ex;
                if (exception.getStatusCode() == 403) {
                    //Todo : make response to front
                }
            }
            return null;
        }
    }
}
