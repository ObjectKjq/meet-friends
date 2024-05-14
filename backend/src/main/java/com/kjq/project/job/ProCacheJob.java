package com.kjq.project.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kjq.project.constant.CacheConstant;
import com.kjq.project.model.entity.NumberStatistics;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.NumberStatisticsService;
import com.kjq.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热
 */
@Component
@Slf4j
public class ProCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;


    @Resource
    private NumberStatisticsService numberStatisticsService;

    /**
     * 每天晚上2:0:0执行执行，查询所有用户的列表对应的推荐列表前20条数据
     */
    @Scheduled(cron = "0 0 23 * * *")
    public void doCacheRecommendUser(){
        RLock lock = redissonClient.getLock(CacheConstant.SCHEDULED_HEARTBEAT_USER_LOCK);
        try {
            //获取锁，为了防止分布式多个项目多次缓存预热，如果有锁返回false
            if(lock.tryLock(0, -1L, TimeUnit.MILLISECONDS)){
                // System.out.println("========="+Thread.currentThread().getId());
                // 获取去日浏览量最高的前100用户的id
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String time = dateFormat.format(new Date());
                Date date = dateFormat.parse(time);
                QueryWrapper<NumberStatistics> numberStatisticsQueryWrapper = new QueryWrapper<>();
                numberStatisticsQueryWrapper.eq("day", date);
                numberStatisticsQueryWrapper.orderByDesc("count");
                numberStatisticsQueryWrapper.last("limit "+ CacheConstant.HEARTBEAT_USER);
                List<NumberStatistics> numberStatistics = numberStatisticsService.list(numberStatisticsQueryWrapper);
                //得到当日浏览量前多少的用户id
                ArrayList<Long> ids = new ArrayList<>();
                for (NumberStatistics numberStatistic : numberStatistics) {
                    ids.add(numberStatistic.getUserId());
                }
                // List<Long> collect = numberStatistics.stream().map(NumberStatistics::getUserId).collect(Collectors.toList());

                //从数据库中查询到这些用户，查询他们的列表执行，预热到缓存中
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.isNotNull("tags"); //淘汰标签为空的列表
                if (ids.isEmpty()){
                    ids.add(-1L);
                }
                userQueryWrapper.in("id", ids);
                List<User> listRecommendUser = userService.list(userQueryWrapper);
                //获取每个用户心动用户前20个
                Map<Long, List<User>> listUsers = userService.matchListUsers(20, listRecommendUser);
                if (ObjectUtils.isEmpty(listUsers)){
                    log.info("没有需要缓存的数据");
                }else{ // listUsers不为空
                    for (Map.Entry<Long, List<User>> entry : listUsers.entrySet()) {
                        Long id = entry.getKey();
                        List<User> users = entry.getValue();
                        //把所查到的数据保存到缓存中去
                        String redisKey = String.format(CacheConstant.CACHE_HEARTBEAT_USER, id);
                        try {
                            redisTemplate.opsForValue().set(redisKey, users, 1, TimeUnit.DAYS);
                        } catch (Exception e) {
                            log.error("redis set key error", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            //判断是不是当前线程加的锁
            if(lock.isHeldByCurrentThread()){
                //System.out.println("========="+Thread.currentThread().getId());
                //释放锁
                lock.unlock();
            }
        }
    }
}
