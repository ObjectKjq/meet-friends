package com.kjq.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjq.project.model.entity.Tag;
import com.kjq.project.service.TagService;
import com.kjq.project.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 86175
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2024-03-13 17:15:16
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




