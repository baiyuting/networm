package com.homework.networm.dao;

import com.homework.networm.NetwormApplicationTests;
import com.homework.networm.dto.WordCountDTO;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class WordCountMapperTest extends NetwormApplicationTests {

    @Resource
    private WordCountMapper wordCountMapper;

    @Test
    public void add(){
        wordCountMapper.add("baiyuting", 1);
    }

    @Test
    public void update(){
        wordCountMapper.update("baiyuting", 2, "2017-11-26");
    }

    @Test
    public void findList(){
        List<WordCountDTO> wordCountDTOS = wordCountMapper.findList( "2017-11-26");
        for (WordCountDTO dto: wordCountDTOS) {
            System.out.println(dto.getWord() + ":" + dto.getCount());
        }
    }
}
