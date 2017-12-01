package com.homework.networm.service;

import com.homework.networm.dao.HrefTextMapper;
import com.homework.networm.dao.WordCountMapper;
import com.homework.networm.dto.WordCountDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class HrefTextService {
    @Resource
    private HrefTextMapper mapper;

    public int add(String text) {
        return mapper.add(text);
    }

}