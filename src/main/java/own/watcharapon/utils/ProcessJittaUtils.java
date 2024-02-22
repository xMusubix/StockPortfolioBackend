package own.watcharapon.utils;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import own.watcharapon.payload.JittaPayload;
import own.watcharapon.payload.SymbolPayload;
import own.watcharapon.repository.WatchlistRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessJittaUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessJittaUtils.class);
    private final String selector1 = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(1) > div:nth-child(1)";
    private final String selector2 = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(2) > div:nth-child(1) > div > div > div:nth-child(1) > div.JittaRanking__FlexItem-sc-1k2m2aq-1.bVCoEZ > a > div";
    private final String selector3 = "#app > div > div.Layout__Content-sc-194vx9d-1.ghuUYV > div > div > div > div.withFooter__ContentWrapper-hhd5n3-1.fKnzex > div.withFooter__ComponentWrapper-hhd5n3-0.bqerfn > div > div > div:nth-child(2) > div:nth-child(1) > div > div > div:nth-child(2) > div.JittaRanking__FlexItem-sc-1k2m2aq-1.bVCoEZ > a > div";

    private final WatchlistRepository watchlistRepository;

    public ProcessJittaUtils(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Async("taskExecutor")
    public void getJittaScore(SymbolPayload symbolPayload) {
        JittaPayload jittaPayload = new JittaPayload();

        try {
            Elements select = Jsoup.connect("https://www.jitta.com/stock/" + symbolPayload.getMarket() + ":" + symbolPayload.getSymbol())
                    .get()
                    .select(selector1);

            String[] splited = select.text().split("\\s+");
            String[] jittaLine = new String[0];
            if (splited.length == 11) {
                jittaLine = splited[7].split("%");
                jittaPayload.setLine(Float.parseFloat(jittaLine[0]));
            } else if (splited.length == 12) {
                jittaLine = splited[8].split("%");
                jittaPayload.setLine(Float.parseFloat(jittaLine[0].replaceAll(",", "")));
            }
            jittaPayload.setScore(Float.parseFloat(splited[5].replaceAll("JITTA", "")));
            jittaPayload.setState(jittaLine[1]);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            watchlistRepository.updateJittaData(symbolPayload, jittaPayload);
        }
    }
}
