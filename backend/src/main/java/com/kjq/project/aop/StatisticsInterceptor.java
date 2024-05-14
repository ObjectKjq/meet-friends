package com.kjq.project.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.model.entity.NumberStatistics;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.NumberStatisticsService;
import com.kjq.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 统计用户当日访问次数
 */
@Aspect
@Component
@Slf4j
public class StatisticsInterceptor {

    @Resource
    private NumberStatisticsService numberStatisticsService;

    @Resource
    private UserService userService;

    /**
     * 给所有方法增加一个浏览量的统计
     */
    @AfterReturning(value = "execution(* com.kjq.project.controller.*.*(..))", returning = "r")
    public void doInterceptor(JoinPoint joinPoint, Object r){
        //判断是否响应成功
        BaseResponse<Object> res = (BaseResponse<Object>) r;
        if(res.getCode() != ErrorCode.SUCCESS.getCode()){
            return;
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User user = null;
        try {
            //如果没有用户不进行统计
            user = userService.getLoginUser(request);
        } catch (Exception e) {
            return;
        }
        try {
            //修改当前用户的浏览量
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = dateFormat.format(new Date());
            Date date = dateFormat.parse(time);
            QueryWrapper<NumberStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("day", date);
            queryWrapper.eq("userId", user.getId());
            NumberStatistics statistics = numberStatisticsService.getOne(queryWrapper);
            //如果当天没有记录，就插入一条
            if(ObjectUtils.isEmpty(statistics)){
                //插入一条记录
                NumberStatistics numberStatistics = new NumberStatistics();
                numberStatistics.setDay(date);
                numberStatistics.setUserId(user.getId());
                numberStatistics.setCount(1);
                numberStatisticsService.save(numberStatistics);
            }else{
                //修改查询到的记录
                statistics.setCount(statistics.getCount() + 1);
                UpdateWrapper<NumberStatistics> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("day", statistics.getDay());
                updateWrapper.eq("userId", statistics.getUserId());
                numberStatisticsService.update(statistics, updateWrapper);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
