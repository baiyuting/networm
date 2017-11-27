package com.homework.networm.dao;

import com.homework.networm.domain.HrefText;
import com.homework.networm.dto.WordCountDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HrefTextMapper {

    @Insert("insert into href_text(text, flag) values(#{text}, 0)")
    int add(@Param("text") String text);

    @Update("update href_text set flag = #{flag} where id = #{id}")
    int update(@Param("flag") Integer flag, @Param("id") Long id);

    @Select("select id, text, flag from href_text where flag=0 ORDER BY id asc limit #{start},#{pageSize};")
    List<HrefText> getPageList( @Param("start") Long start, @Param("pageSize") Integer pageSize);

    @Select("select min(id) from href_text where flag=0")
    Long getMinId();
}