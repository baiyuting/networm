package com.homework.networm.service;

import com.homework.networm.NetwormApplicationTests;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

public class LoadDbServiceTest extends NetwormApplicationTests {

    @Resource
    private LoadDbService loadDbService;

    @Test
    public void load() throws IOException {
        loadDbService.load();
    }

    @Test
    public void testSplit(){
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
//        System.out.println(ToAnalysis.parse(str));

        Result result = ToAnalysis.parse(str);
        List<Term> list = result.getTerms();
        for (Term term : list) {
            System.out.print(term.getName() + "|");
        }
    }


}
