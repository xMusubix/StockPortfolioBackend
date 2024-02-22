package own.watcharapon.utils;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import own.watcharapon.payload.SymbolDataPayload;

import java.util.Map;

@Component
public class SymbolDataUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SymbolDataUtils.class);
    //    private static final String MARKET_SELECTOR = "//*[@id=\"root\"]/div[1]/div[1]/div[1]/main[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[3]/div[1]/span";
    private static final String MARKET_SELECTOR = "//*[@id=\"root\"]/div[1]/div[1]/div[1]/main[1]/div[5]/div[1]/div[1]/div[1]/div[3]/div[1]/span[1]";
    private static final String SECTOR_SELECTOR = "//*[@class=\"rC_DP\"]/div[1]/div[1]/div[2]/div/a/span";
    private static final String INDUSTRY_SELECTOR = "//*[@class=\"rC_DP\"]/div[1]/div[2]/div[2]/div/a/span";

    public static SymbolDataPayload checkSymbol(String symbol) {
        try {
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";
            Document document = Jsoup.connect("https://seekingalpha.com/symbol/" + symbol)
                    .userAgent(userAgent)
                    .get();

            Elements sectorSelect = document.selectXpath(SECTOR_SELECTOR);
            Elements industrySelect = document.selectXpath(INDUSTRY_SELECTOR);
            Elements marketSelect = document.selectXpath(MARKET_SELECTOR);

            String sectorString = sectorSelect.text();
            if (!sectorString.startsWith("HTTP") && !sectorString.isEmpty()) {
                LOG.info("{} Sector : {}", symbol, sectorString);
            }

            String industryString = industrySelect.text();
            if (!industryString.startsWith("HTTP") && !industryString.isEmpty()) {
                LOG.info("{} Industry : {}", symbol, industryString);
            }

            String marketString = marketSelect.text();
            if (!marketString.startsWith("HTTP") && !marketString.isEmpty()) {
                marketString = marketString.split(" \\|")[0].trim();
                LOG.info("{} Market : {}", symbol, marketString);
            }

            return new SymbolDataPayload(sectorString, industryString, marketString);
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
