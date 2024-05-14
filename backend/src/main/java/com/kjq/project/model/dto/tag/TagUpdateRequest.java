package com.kjq.project.model.dto.tag;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class TagUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 上传标签的用户
     */
    private Long userId;

    /**
     * 该标签是哪个分类下的
     */
    private Long parentId;

    /**
     * 是否为分类标签
     */
    private Integer isParent;

    private static final long serialVersionUID = 1L;
}