package com.homework.networm.dao;

import com.homework.networm.dto.WordCountDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface WordCountMapper {

    @Insert("insert into word_count(word, count) values(#{word}, #{count})")
    int add(@Param("word") String word, @Param("count") Integer count);

    @Update("update word_count set count = #{count} where word = #{word} and date(create_time)=#{day}")
    int update(@Param("word") String word, @Param("count") Integer count, @Param("day") String day);

    @Update("update word_count set flag=1 where id=#{id}")
    int readed(@Param("id") Long id);

    @Select("select word, count from word_count where date(create_time)=#{day} ORDER BY count desc limit 0,50;")
    List<WordCountDTO> findList(String day);

}