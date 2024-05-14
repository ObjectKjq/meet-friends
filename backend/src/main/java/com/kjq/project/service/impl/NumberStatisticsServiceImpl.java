package com.kjq.project.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjq.project.model.dto.statistics.StatisticsPieChartDto;
import com.kjq.project.model.entity.NumberStatistics;
import com.kjq.project.service.NumberStatisticsService;
import com.kjq.project.mapper.NumberStatisticsMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author 86175
* @description 针对表【number_statistics】的数据库操作Service实现
* @createDate 2024-03-22 22:17:44
*/
@Service
public class NumberStatisticsServiceImpl extends ServiceImpl<NumberStatisticsMapper, NumberStatistics>
    implements NumberStatisticsService{

    @Override
    public List<StatisticsPieChartDto> getNumber() {
        //获取昨天的日期
        Date yesterday = DateUtil.yesterday();
        String time = DateUtil.formatDate(yesterday);
        return baseMapper.getNumber(time);
    }
}




