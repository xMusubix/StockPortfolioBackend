package own.watcharapon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import own.watcharapon.service.AssetsService;

@Component
public class ProcessJob {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessJob.class);
    private final AssetsService assetsService;

    public ProcessJob(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @Scheduled(cron = "0 */2 20-23,0-4 * * TUE-FRI") // every 2 minute from 8 PM to 4 AM (GMT+7) on weekdays
    public void doTaskOnWeekdays() {
        assetsService.updateLatestPriceData();
    }

    @Scheduled(cron = "0 */2 20-23 * * MON") // every 2 minute from 8 PM to 4 AM (GMT+7) on weekdays
    public void doTaskOnWeekdaysMon() {
        assetsService.updateLatestPriceData();
    }

    @Scheduled(cron = "0 */2 0-4 * * SAT") // every 2 minute from 8 PM to 4 AM (GMT+7) on weekdays
    public void doTaskOnWeekdaysSat() {
        assetsService.updateLatestPriceData();
    }
}
