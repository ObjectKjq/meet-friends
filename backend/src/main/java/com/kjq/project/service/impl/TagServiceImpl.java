package com.kjq.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.constant.CacheConstant;
import com.kjq.project.model.entity.Tag;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.TagService;
import com.kjq.project.mapper.TagMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 86175
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2024-03-13 17:15:16
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Resource
    TagMapper tagMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取全部标签数据
     * @return
     */
    @Override
    public List<Object> listTag(){
        //先从缓存中取，如果没有再查询数据库
        String redisKey = String.format(CacheConstant.CACHE_TAG);
        List<Object> list = (List<Object>) redisTemplate.opsForValue().get(redisKey);
        if (list != null){
            return list;
        }
        //查询数据库
        List<Object> resultList = new ArrayList<>();
        QueryWrapper<Tag> parentWrapper = new QueryWrapper<>();
        parentWrapper.eq("isParent", 1);
        List<Tag> tags = tagMapper.selectList(parentWrapper);
        for (Tag tag : tags) {
            List<Object> tagsChild = new ArrayList<>();
            tagsChild.add(tag);
            QueryWrapper<Tag> parentWrapperChild = new QueryWrapper<>();
            parentWrapperChild.eq("isParent", 0);
            parentWrapperChild.eq("parentId", tag.getId());
            List<Tag> tagsChildChild = tagMapper.selectList(parentWrapperChild);
            tagsChild.add(tagsChildChild);
            resultList.add(tagsChild);
        }
        //保存到缓存中去
        try {
            redisTemplate.opsForValue().set(redisKey, resultList, 1000*60*60*24, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return resultList;
    }
}




