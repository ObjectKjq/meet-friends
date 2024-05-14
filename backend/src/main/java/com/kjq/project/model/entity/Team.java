package com.kjq.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team implements Serializable {
    /**
     * 队伍的主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 队伍的名称
     */
    private String name;

    /**
     * 队伍的描述
     */
    private String description;

    /**
     * 队伍能加入的最大人数
     */
    private Integer maxNum;

    /**
     * 队伍的过期时间
     */
    private Date expireTime;

    /**
     * 队长
     */
    private Long userId;

    /**
     * 队伍状态0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 加密后队伍密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}