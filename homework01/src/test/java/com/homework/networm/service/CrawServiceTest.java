package com.homework.networm.service;

import com.homework.networm.NetwormApplicationTests;
import org.junit.Test;

import javax.annotation.Resource;

public class CrawServiceTest extends NetwormApplicationTests {

    @Resource
    private CrawService crawService;

    @Test
    public void craw(){
        crawService.craw();
    }
}
