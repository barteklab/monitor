package trades;

import lombok.extern.slf4j.Slf4j;
import messaging.Publisher;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.util.List;

@Slf4j
public class MonitorJob implements Job {
    private TradeReader reader = new TradeReader();
    private Publisher publisher;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Quartz job started");
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        publisher = new Publisher(dataMap.getString(Constants.KAFKA_BOOTSTRAP_SERVERS), dataMap.getString(Constants.KAFKA_TOPIC));

        List<Trade> trades = reader.readTrades(dataMap.getString(Constants.INPUTFILES_PATH));
        publisher.publish(trades);
    }
}
