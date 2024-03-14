package own.watcharapon.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import own.watcharapon.service.AssetsService;
import own.watcharapon.service.ExchangeRateService;
import own.watcharapon.service.WatchlistService;
import own.watcharapon.utils.ExchangeRateUtils;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncConfiguration.class);
    private final WatchlistService watchlistService;
    private final AssetsService assetsService;
    private final ExchangeRateService exchangeRateService;

    public AsyncConfiguration(@Lazy WatchlistService watchlistService, @Lazy AssetsService assetsService, ExchangeRateService exchangeRateService) {
        this.watchlistService = watchlistService;
        this.assetsService = assetsService;
        this.exchangeRateService = exchangeRateService;
    }

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        LOG.info("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("Thread-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "jittaExecutor")
    public Executor customExecutor() {
        LOG.info("Creating Jitta Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Adjust as needed
        executor.setMaxPoolSize(5); // Adjust as needed
        executor.setQueueCapacity(500); // Adjust as needed
        executor.setThreadNamePrefix("Jitta-Thread-");
        executor.initialize();
        return executor;
    }

    @PostConstruct
    void postConstruct() {
        watchlistService.updateJittaData();
        assetsService.updateAllCostAndShare();
        assetsService.updateHistoryPriceData();
        assetsService.updateLatestPriceData();
        assetsService.updateDividendData();
        exchangeRateService.updateExchangeRate();
    }
}
