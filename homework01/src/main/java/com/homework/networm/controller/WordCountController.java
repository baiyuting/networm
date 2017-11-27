package com.homework.networm.controller;

import com.homework.networm.dto.WordCountDTO;
import com.homework.networm.service.WordCountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/wordCount")
public class WordCountController {

    @Resource
    WordCountService accountService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<WordCountDTO> getAccounts() {
        return accountService.getList();
    }

}