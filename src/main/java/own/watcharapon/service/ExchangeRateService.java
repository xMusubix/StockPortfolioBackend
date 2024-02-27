package own.watcharapon.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import own.watcharapon.repository.ExchangeRateRepository;
import own.watcharapon.utils.ExchangeRateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ExchangeRateService {
    private static final Logger LOG = LoggerFactory.getLogger(AssetsService.class);
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateUtils exchangeRateUtils;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, ExchangeRateUtils exchangeRateUtils) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateUtils = exchangeRateUtils;
    }

    public void updateExchangeRate() {
        LOG.info("Start Update Exchange Rate Data");
        LocalDate lastUpdateExchangeRate = exchangeRateRepository.getLastUpdate();
        if (lastUpdateExchangeRate == null || lastUpdateExchangeRate.isBefore(LocalDate.now())) {
            exchangeRateUtils.updateExchangeRate();
        }
    }

    public Double getLastExchangeRate() {
        return exchangeRateRepository.getLastExchangeRate();
    }
}
