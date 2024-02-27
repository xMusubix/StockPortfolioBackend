package own.watcharapon.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import own.watcharapon.entity.ExchangeRateEntity;
import own.watcharapon.payload.BotExchangeRatePayload;
import own.watcharapon.repository.ExchangeRateRepository;

import java.time.LocalDate;

@Component
public class ExchangeRateUtils {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateUtils(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public void updateExchangeRate() {
        RestClient restClient = RestClient.create();
        String uri = String.format("https://apigw1.bot.or.th/bot/public/Stat-ExchangeRate/v2/DAILY_AVG_EXG_RATE/?start_period=%s&end_period=%s&currency=USD", LocalDate.now().minusDays(7), LocalDate.now());
        BotExchangeRatePayload result = restClient.get()
                .uri(uri)
                .header("X-IBM-Client-Id", "e3f306fd-fef6-4cd7-8da6-e2892e7cd0df")
                .retrieve()
                .body(BotExchangeRatePayload.class);
        LocalDate period = LocalDate.parse(result.getResult().getData().getData_detail().get(0).getPeriod());
        Double rate = Double.valueOf(result.getResult().getData().getData_detail().get(0).getMid_rate());
        exchangeRateRepository.save(new ExchangeRateEntity(period, rate));
    }
}
