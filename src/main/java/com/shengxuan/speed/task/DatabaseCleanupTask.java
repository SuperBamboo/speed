package com.shengxuan.speed.task;

import com.shengxuan.speed.mapper.PushAlarmMapper;
import com.shengxuan.speed.util.DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DatabaseCleanupTask {
    private final PushAlarmMapper pushAlarmMapper;

    public DatabaseCleanupTask(PushAlarmMapper pushAlarmMapper) {
        this.pushAlarmMapper = pushAlarmMapper;
    }

    @Scheduled(cron = "0 0 0 1 */2 ?")  //每两个月的1号执行一次
    @Transactional
    public void cleanUpOldData(){
        LocalDate date2MonthAgo = LocalDate.now().minusMonths(2);
        String format = date2MonthAgo.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        pushAlarmMapper.deleteDateAgo(format);
    }
}
