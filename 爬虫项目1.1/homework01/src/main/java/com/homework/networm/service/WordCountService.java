package com.homework.networm.service;

import com.homework.networm.dao.WordCountMapper;
import com.homework.networm.dto.WordCountDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class WordCountService {
    @Resource
    private WordCountMapper wordCountMapper;

    public int add(String word, Integer count) {
        return wordCountMapper.add(word, count);
    }
    public int update(String word, Integer count) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String day = formatter.format(date);
        return wordCountMapper.update(word, count, day);
    }
    public List<WordCountDTO> getList() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String day = formatter.format(date);
        return wordCountMapper.findList(day);
    }
}