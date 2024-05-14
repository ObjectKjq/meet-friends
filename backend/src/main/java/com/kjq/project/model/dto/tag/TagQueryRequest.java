package com.kjq.project.model.dto.tag;

import com.kjq.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagQueryRequest extends PageRequest implements Serializable {

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