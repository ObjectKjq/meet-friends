package com.kjq.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjq.project.annotation.AuthCheck;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.DeleteRequest;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.model.dto.tag.TagAddRequest;
import com.kjq.project.model.dto.tag.TagQueryRequest;
import com.kjq.project.model.dto.tag.TagUpdateRequest;
import com.kjq.project.model.entity.Tag;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.TagService;
import com.kjq.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @Resource
    private UserService userService;

    /**
     * 用户获取标签列表
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Object>> listTag(){
        List<Object> list = tagService.listTag();
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(list);
    }

    /**
     * 创建
     *
     * @param tagAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Long> addTag(@RequestBody TagAddRequest tagAddRequest, HttpServletRequest request) {
        if (tagAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagAddRequest, tag);
        // todo 校验
        // tagService.validTag(tag, true);
        User loginUser = userService.getLoginUser(request);
        tag.setUserId(loginUser.getId());
        boolean result = tagService.save(tag);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        long newTagId = tag.getId();
        return ResultUtils.success(newTagId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> deleteTag(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Tag oldTag = tagService.getById(id);
        if (oldTag == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldTag.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        boolean b = tagService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param tagUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest,
                                            HttpServletRequest request) {
        if (tagUpdateRequest == null || tagUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagUpdateRequest, tag);
        // todo 参数校验
        // tagService.validTag(tag, false);
        User user = userService.getLoginUser(request);
        long id = tagUpdateRequest.getId();
        // 判断是否存在
        Tag oldTag = tagService.getById(id);
        if (oldTag == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldTag.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        boolean result = tagService.updateById(tag);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Tag> getTagById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tag = tagService.getById(id);
        return ResultUtils.success(tag);
    }

    /**
     * 分页获取列表
     *
     * @param tagQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Page<Tag>> listTagByPage(TagQueryRequest tagQueryRequest, HttpServletRequest request) {
        if (tagQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Tag tagQuery = new Tag();
        BeanUtils.copyProperties(tagQueryRequest, tagQuery);
        long current = tagQueryRequest.getCurrent();
        long size = tagQueryRequest.getPageSize();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(tagQuery.getTagName())){
            queryWrapper.like("tagName", tagQuery.getTagName());
        }
        Page<Tag> tagPage = tagService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(tagPage);
    }

}
