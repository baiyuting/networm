package com.homework.networm.task;

import com.homework.networm.service.CrawService;
import com.homework.networm.service.LoadDbService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Task {

    @Resource
    private CrawService crawService;

    @Resource
    private LoadDbService loadDbService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void craw() {
        //凌晨1点爬虫
        crawService.craw();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void load() {
        //凌晨3点处理爬虫数据，并且将处理的词频数据放入 word_count 表中
        try {
            loadDbService.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}