package com.homework.networm.service;

import com.homework.networm.dao.HrefTextMapper;
import com.homework.networm.dao.WordCountMapper;
import com.homework.networm.domain.HrefText;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class LoadDbService {

    @Resource
    private HrefTextMapper textMapper;

    @Resource
    private WordCountMapper countMapper;

    public void load() throws IOException {
        Map<String, Integer> keywordCounts = new HashMap<String, Integer>();

        Long minId = textMapper.getMinId() - 1;
        Integer pageSize = 500;
        List<HrefText> texts = textMapper.getPageList(minId, pageSize);
        while (!CollectionUtils.isEmpty(texts)) {
            for (int i = 0; i < texts.size(); i++) {
                HrefText hrefText = texts.get(i);
                String text = hrefText.getText();
                //改href文本设置为已经度过
                textMapper.update(1, hrefText.getId());

                // 进行分词操作
                List<Term> terms = ToAnalysis.parse(text).getTerms();
                List<String> allKeywords = new ArrayList<>();
                for (int j = 0; j < terms.size(); j++) {
                    Term term = terms.get(j);
                    if (null != term.getName() && !"".equals(term.getName().trim()) && term.getName().length() > 1) {
                        allKeywords.add(term.getName());
                    }
                }
                for (String keyword : allKeywords) {
                    // 先判断这个词在Map中是否已经保存过了
                    if (keywordCounts.containsKey(keyword)) {
                        // 如果之前保存过，在原有的出现次数基础上 ＋ 1
                        keywordCounts.put(keyword, keywordCounts.get(keyword) + 1);
                    } else {
                        // 这是第一次出现
                        keywordCounts.put(keyword, 1);
                    }
                }
            }
            minId = texts.get(texts.size() - 1).getId();
            texts = textMapper.getPageList(minId, pageSize);
        }

        Set<String> allKeys = keywordCounts.keySet();
        for (String key : allKeys) {
            int value = keywordCounts.get(key);
            //添加词频统计
            countMapper.add(key, value);
        }

    }

}
