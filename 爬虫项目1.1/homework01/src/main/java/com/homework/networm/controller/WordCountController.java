package com.homework.networm.controller;

import com.homework.networm.dto.WordCountDTO;
import com.homework.networm.service.WordCountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.ResponseWrapper;
import java.util.List;

@RestController
@RequestMapping("/wordCount")
public class WordCountController {

    @Resource
    WordCountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET )
    public List<WordCountDTO> getAccounts(HttpServletResponse response) {
        System.out.println("--------->被访问");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return accountService.getList();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET )
    public String test(HttpServletResponse response) {
        System.out.println("--------->被访问2");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return "{\"test\":2}";
    }

}