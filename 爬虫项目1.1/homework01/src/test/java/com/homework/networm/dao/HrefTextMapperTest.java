package com.homework.networm.dao;

import com.homework.networm.NetwormApplicationTests;
import com.homework.networm.domain.HrefText;
import com.homework.networm.dto.WordCountDTO;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class HrefTextMapperTest extends NetwormApplicationTests {

    @Resource
    private HrefTextMapper mapper;

    @Test
    public void add(){
        mapper.add("baiyuting");
    }

    @Test
    public void update(){
        mapper.update(1, 1L);
    }

    @Test
    public void getPageList(){
        List<HrefText> texts = mapper.getPageList(0L, 1);
        for (int i = 0; i < texts.size(); i++) {
            System.out.println(texts.get(i).getId());
        }
    }

    @Test
    public void getMinId(){
        Long minId = mapper.getMinId();
        System.out.println(minId);
    }
}
