package own.watcharapon.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import own.watcharapon.payload.AssetsDividendDataPayload;
import own.watcharapon.repository.AssetsRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Component
public class ProcessDividendUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDividendUtils.class);
    private final AssetsRepository assetsRepository;

    public ProcessDividendUtils(AssetsRepository assetsRepository) {
        this.assetsRepository = assetsRepository;
    }

    @Async("taskExecutor")
    public void updateDividendData(List<String> marketSymbolList) {
        String exDateXpath = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[1]/div[1]/span";
        String amountXpath = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[3]/div[1]/span";
        String dividendYieldXpath = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[5]/div[1]/span";
        String payoutRatioXpath = "//*[@id=\"tr-stock-page-content\"]/div[1]/div[4]/div[1]/div[2]/div[2]/div[2]/div[7]/div[1]/span";
        int count = 0;
        for (String marketSymbol : marketSymbolList) {
            try {
                AssetsDividendDataPayload assetsDividendDataPayload = new AssetsDividendDataPayload();
                assetsDividendDataPayload.setMarketSymbol(marketSymbol);
                String symbol = marketSymbol.split("#")[1];
                Document document = Jsoup
                        .connect("https://www.tipranks.com/stocks/" + symbol + "/dividends")
                        .get();

                Elements exDateElements = document.selectXpath(exDateXpath);
                String exDateString = exDateElements.text();
                if (!exDateString.startsWith("HTTP") && !exDateString.isEmpty()) {
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .parseCaseInsensitive()
                            .appendPattern("MMM dd, yyyy")
                            .toFormatter(Locale.ENGLISH);
                    LocalDate result = LocalDate.parse(exDateString, formatter);
                    LOG.info("{} Ex-Date : {}", symbol, result.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    assetsDividendDataPayload.setExDate(result);
                } else {
                    assetsDividendDataPayload.setExDate(null);
                }

                Elements amountElements = document.selectXpath(amountXpath);
                String amountString = amountElements.text();
                if (!amountString.startsWith("HTTP") && !amountString.isEmpty()) {
                    LOG.info("{} Dividend Amount : {}", symbol, amountString);
                    amountString = amountString.replace("$", "");
                    assetsDividendDataPayload.setDividendAmount(StringUtils.hasText(amountString) ? Double.parseDouble(amountString) : null);
                }

                Elements dividendYieldElements = document.selectXpath(dividendYieldXpath);
                String dividendYieldString = dividendYieldElements.text();
                if (!dividendYieldString.startsWith("HTTP") && !dividendYieldString.isEmpty()) {
                    LOG.info("{} Dividend Yield : {}", symbol, dividendYieldString);
                    dividendYieldString = dividendYieldString.replace("%", "");
                    if (StringUtils.hasText(dividendYieldString) && !dividendYieldString.equals("―")) {
                        assetsDividendDataPayload.setDividendYield(Double.parseDouble(dividendYieldString));
                    } else {
                        assetsDividendDataPayload.setDividendYield(null);
                    }
                }

                Elements payoutRatioElements = document.selectXpath(payoutRatioXpath);
                String payoutRatioString = payoutRatioElements.text();
                if (!payoutRatioString.startsWith("HTTP") && !payoutRatioString.isEmpty()) {
                    LOG.info("{} Payout Ratio : {}", symbol, payoutRatioString);
                    payoutRatioString = payoutRatioString.replace("%", "");
                    if (StringUtils.hasText(payoutRatioString) && !payoutRatioString.equals("N/A")) {
                        assetsDividendDataPayload.setPayoutRatio(Double.parseDouble(payoutRatioString));
                    } else {
                        assetsDividendDataPayload.setPayoutRatio(null);
                    }
                }

                assetsRepository.updateDividendPrice(assetsDividendDataPayload);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (++count == 5 && marketSymbolList.indexOf(marketSymbol) != marketSymbolList.size() - 1) {
                try {
                    LOG.info("Sleep");
                    TimeUnit.MINUTES.sleep(1);
                    count = 0;
                } catch (InterruptedException e) {
                    LOG.error("Interrupted while waiting for next iteration", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
