package trades;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static trades.Constants.*;

@Slf4j
public class Monitor {
    public static final String MONITOR_PROPERTIES = "monitor.properties";
    public static final String MONITOR_JOB = "monitorJob";
    public static final String TRIGGER = "trigger";
    public static final String GROUP_1 = "group1";
    public static final String MONITORING_INTERVAL = "monitor.interval";


    public static void main(String[] args) throws SchedulerException {
        Monitor monitor = new Monitor();
        monitor.start();
    }

    private void start() throws SchedulerException {
        Properties properties = loadProperties();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();

        JobDetail job = getJobDetail(properties);
        Trigger trigger = getTrigger(properties.getProperty(MONITORING_INTERVAL));
        scheduler.scheduleJob(job, trigger);
    }

    private JobDetail getJobDetail(Properties properties) {
        return JobBuilder.newJob(MonitorJob.class)
                .withIdentity(MONITOR_JOB, GROUP_1)
                .usingJobData(KAFKA_TOPIC, properties.getProperty(KAFKA_TOPIC))
                .usingJobData(KAFKA_BOOTSTRAP_SERVERS, properties.getProperty(KAFKA_BOOTSTRAP_SERVERS))
                .usingJobData(INPUTFILES_PATH, properties.getProperty(INPUTFILES_PATH))
                .build();
    }

    private Trigger getTrigger(String interval) {
        Integer intervalInt = Integer.valueOf(interval);
        return TriggerBuilder.newTrigger()
                .withIdentity(TRIGGER, GROUP_1)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(intervalInt)
                        .repeatForever())
                .build();
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = Monitor.class.getClassLoader().getResourceAsStream(MONITOR_PROPERTIES)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
