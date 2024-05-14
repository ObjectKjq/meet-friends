package com.kjq.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjq.project.annotation.AuthCheck;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.constant.CacheConstant;
import com.kjq.project.constant.UserConstant;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.manager.MinioManager;
import com.kjq.project.model.dto.user.UserAddRequest;
import com.kjq.project.model.dto.user.UserQueryRequest;
import com.kjq.project.model.entity.User;
import com.kjq.project.model.dto.user.UserLoginRequest;
import com.kjq.project.model.dto.user.UserRegisterRequest;
import com.kjq.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private MinioManager minioManager;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //controller倾向于对参数本身进行校验，不涉及业务逻辑，
        // 为什么再service中也要写，因为service可能不止调用一次
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        //controller倾向于对参数本身进行校验，不涉及业务逻辑，
        // 为什么再service中也要写，因为service可能不止调用一次
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 管理员查询用户的列表
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/search")
    public BaseResponse<Page<User>> searchUsers(UserQueryRequest userQueryRequest){
        if (ObjectUtils.isEmpty(userQueryRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userQueryRequest.getUserName())){
            queryWrapper.like("username", userQueryRequest.getUserName());
        }
        long current = 1;
        long size = 2;
        if (userQueryRequest.getCurrent() != 0 && userQueryRequest.getPageSize() != 0){
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);

        List<User> users = userPage.getRecords().stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(userPage.setRecords(users));
    }

    /**
     * 管理员删除用户
     * @param id
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id){
        if(id <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数错误");
        }
        //逻辑删除
        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 管理员添加用户
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/add")
    public BaseResponse<Boolean> addUser(@Validated UserAddRequest userAddRequest){
        //参数校验，校验每个参数是否为空
        if (ObjectUtils.isEmpty(userAddRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //验证用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String userAccount = userAddRequest.getUserAccount();
        queryWrapper.eq("userAccount", userAccount);
        long count = userService.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        //文件上传返回地址
        String avatarUrl = minioManager.upload(userAddRequest.getAvatarFile());
        //添加到数据库
        userAddRequest.setAvatarUrl(avatarUrl);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        if (!userService.save(user)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 只有登录的用户才能获取到信息
     * @param request
     * @return
     */
    @GetMapping("/info")
    public BaseResponse<User> getUser(HttpServletRequest request){
        User attribute = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(attribute == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户没有登陆");
        }
        return ResultUtils.success(userService.getSafetyUser(userService.getLoginUser(request)));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success(true);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagList){
        if(CollectionUtils.isEmpty(tagList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUserByTags(tagList);
        return ResultUtils.success(userList);
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request){
        //1.检查用户是否为空
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Integer result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 用户首页顺序获取，每次用户点击下一页来获取数据
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(UserQueryRequest userQueryRequest){
        long current = 1;
        long size = 2;
        User userQuery = new User();
        if (userQueryRequest != null) {
            BeanUtils.copyProperties(userQueryRequest, userQuery);
            current = userQueryRequest.getCurrent();
            size = userQueryRequest.getPageSize();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userPage);
    }

    /**
     * 获取最匹配的用户，前20条数据
     *
     * @param num
     * @param request
     * @return
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        if(user == null || user.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //先从缓存中取，如果没有再查询数据库
        String redisKey = String.format(CacheConstant.CACHE_HEARTBEAT_USER, user.getId());
        List<User> userCache = (List<User>) redisTemplate.opsForValue().get(redisKey);
        if (userCache != null){
            return ResultUtils.success(userCache);
        }
        //查询推荐数据
        BaseResponse<List<User>> response = ResultUtils.success(userService.matchUsers(num, user));
        //保存到缓存中去
        try {
            redisTemplate.opsForValue().set(redisKey, response.getData(), 1, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return response;
    }
}
