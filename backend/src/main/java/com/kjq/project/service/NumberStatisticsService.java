package com.kjq.project.service;

import com.kjq.project.model.dto.statistics.StatisticsPieChartDto;
import com.kjq.project.model.entity.NumberStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86175
* @description 针对表【number_statistics】的数据库操作Service
* @createDate 2024-03-22 22:17:44
*/
public interface NumberStatisticsService extends IService<NumberStatistics> {

    /**
     * 统计昨天前10名浏览量最高的用户
     * @return
     */
    List<StatisticsPieChartDto> getNumber();
}
