package com.kjq.project.model.dto.tag;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class TagAddRequest implements Serializable {

    /**
     * 标签名称
     */
    private String tagName;


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