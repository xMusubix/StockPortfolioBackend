package own.watcharapon;


import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.account.Account;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.trade.Trade;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarAdjustment;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarFeed;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.snapshot.Snapshot;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.trade.StockTrade;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.trade.StockTradesResponse;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        AlpacaAPI alpacaAPI = new AlpacaAPI();

//        String selector = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[1]/div[1]/span";
//        String selector2 = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[3]/div[1]/span";
//        String selector3 = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[5]/div[1]/span";
//        String selector4 = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[7]/div[1]/span";
//        String selector5 = "//*[@class=\"rC_DP\"]/div[1]/div[1]/div[2]/div/a/span";
//        String selector6 = "//*[@class=\"rC_DP\"]/div[1]/div[2]/div[2]/div/a/span";
//        try {
//            Document document = Jsoup
//                    .connect("https://www.tipranks.com/stocks/" + "AAPL" + "/dividends")
//                    .get();
//            Elements select = document.selectXpath(selector);
//            Elements select2 = document.selectXpath(selector2);
//            Elements select3 = document.selectXpath(selector3);
//            Elements select4 = document.selectXpath(selector4);
//            String dateString = select.text();
//            String amountString = select2.text();
//            String dividendYieldString = select3.text();
//            String payoutRatioString = select4.text();
//            if (!dateString.startsWith("HTTP") && !dateString.isEmpty()) {
//                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                        .parseCaseInsensitive()
//                        .appendPattern("MMM dd, yyyy")
//                        .toFormatter(Locale.ENGLISH);
//                LocalDate result = LocalDate.parse(dateString, formatter);
//                LOG.info("AAPL Ex-Date : " + result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//            }
//
//            if (!amountString.startsWith("HTTP") && !amountString.isEmpty()) {
//                LOG.info("AAPL Dividend Amount : " + amountString);
//            }
//
//            if (!dividendYieldString.startsWith("HTTP") && !dividendYieldString.isEmpty()) {
//                LOG.info("AAPL Dividend Yield : " + dividendYieldString);
//            }
//
//            if (!payoutRatioString.startsWith("HTTP") && !payoutRatioString.isEmpty()) {
//                LOG.info("AAPL Payout Ratio : " + payoutRatioString);
//            }


//            Document document2 = Jsoup
//                    .connect("https://seekingalpha.com/symbol/" + "AAPL")
//                    .get();
//            Elements select5 = document2.selectXpath(selector5);
//            Elements select6 = document2.selectXpath(selector6);
//
//            String sectorString = select5.text();
//            if (!sectorString.startsWith("HTTP") && !sectorString.isEmpty()) {
//                LOG.info("AAPL Sector : " + sectorString);
//            }
//            String industryString = select6.text();
//            if (!industryString.startsWith("HTTP") && !industryString.isEmpty()) {
//                LOG.info("AAPL Industry : " + industryString);
//            }
//        } catch (IOException e) {
//            LOG.warn(e.getMessage());
//        }
//        try {
//            StockBarsResponse StockBarsResponse = alpacaAPI.stockMarketData().getBars(
//                    "TSLA",
//                    ZonedDateTime.of(2023, 1, 17, 0, 0, 0, 0, ZoneId.of("America/New_York")),
//                    ZonedDateTime.of(2024, 1, 22, 0, 0, 0, 0, ZoneId.of("America/New_York")),
//                    null,
//                    null,
//                    1,
//                    BarTimePeriod.DAY,
//                    BarAdjustment.SPLIT,
//                    BarFeed.SIP);
//            for (StockBar stockBar : StockBarsResponse.getBars()) {
//                System.out.println("Time : " + stockBar.getTimestamp() + "\t" + "Price : " + stockBar.getClose());
//            }
//        } catch (AlpacaClientException exception) {
//            exception.printStackTrace();
//        }
    }
}