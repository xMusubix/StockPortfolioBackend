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
import own.watcharapon.service.WatchlistService;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncConfiguration.class);
    private final WatchlistService watchlistService;

    public AsyncConfiguration(@Lazy WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
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

    @PostConstruct
    void postConstruct() {
        watchlistService.updateJittaData();
    }
}
