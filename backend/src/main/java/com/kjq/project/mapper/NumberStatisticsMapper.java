package com.kjq.project.mapper;

import com.kjq.project.model.dto.statistics.StatisticsPieChartDto;
import com.kjq.project.model.entity.NumberStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 86175
* @description 针对表【number_statistics】的数据库操作Mapper
* @createDate 2024-03-22 22:17:44
* @Entity com.kjq.project.model.entity.NumberStatistics
*/
public interface NumberStatisticsMapper extends BaseMapper<NumberStatistics> {

    @Select("SELECT number_statistics.count AS `value`, user.username AS `name` " +
            "FROM number_statistics " +
            "JOIN `user` ON user.id = number_statistics.userId " +
            "WHERE `day` = #{yesterday} " +
            "ORDER BY `count` " +
            "DESC LIMIT 10")
    List<StatisticsPieChartDto> getNumber(String yesterday);
}




