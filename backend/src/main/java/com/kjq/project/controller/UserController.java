package com.kjq.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.contant.UserContent;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.model.entity.User;
import com.kjq.project.model.request.UserLoginRequest;
import com.kjq.project.model.request.UserRegisterRequest;
import com.kjq.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

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

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH, "用户没有访问权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(users);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        //管理员查询
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH, "用户没有访问权限");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数错误");
        }
        //逻辑删除
        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 只有登录的用户才能获取到信息
     * @param request
     * @return
     */
    @GetMapping("/info")
    public BaseResponse<User> getUser(HttpServletRequest request){
        User attribute = (User) request.getSession().getAttribute(UserContent.USER_LOGIN_STATE);
        if(attribute == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户没有登陆");
        }
        long userId = attribute.getId();
        User user = userService.getById(userId);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        request.getSession().removeAttribute(UserContent.USER_LOGIN_STATE);
        return ResultUtils.success(true);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //管理员查询
        User user = (User) request.getSession().getAttribute(UserContent.USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserContent.ADMIN_ROLE;
    }
}
