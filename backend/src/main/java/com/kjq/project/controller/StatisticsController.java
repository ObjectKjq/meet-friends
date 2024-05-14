package com.kjq.project.controller;

import com.kjq.project.annotation.AuthCheck;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.model.dto.statistics.StatisticsPieChartDto;
import com.kjq.project.service.NumberStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private NumberStatisticsService numberStatisticsService;

    @AuthCheck(mustRole = "admin")
    @GetMapping("/number")
    public BaseResponse<List<StatisticsPieChartDto>> getNumber(){
        List<StatisticsPieChartDto> numberStatisticsList = numberStatisticsService.getNumber();
        return ResultUtils.success(numberStatisticsList);
    }
}
