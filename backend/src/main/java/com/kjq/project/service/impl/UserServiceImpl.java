package com.kjq.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.contant.UserContent;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.UserService;
import com.kjq.project.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 86175
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-03-11 00:44:20
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    //腌值
    private static final String SALT = "yupi";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        //2.校验账户
        if(userAccount.length() < 3){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if(userPassword.length() < 3 || checkPassword.length() < 3){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不相同");
        }

        //账户不能重复
        //指定查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //判断userAccount没有有等于userAccount的值的
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        //加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //向数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean save = this.save(user);
        if(!save){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统错误");
        }
        return user.getId();
    }

    @Override
    public BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        //2.校验账户
        if(userAccount.length() < 3){
            throw new BusinessException(ErrorCode.NULL_ERROR, "账号太短");
        }
        if(userPassword.length() < 3){
            throw new BusinessException(ErrorCode.NULL_ERROR, "密码太短");
        }
        //加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //判断userAccount没有有等于userAccount的值的
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            log.info("user login failed, 用户名或密码错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败");
        }
        //脱密
        User safetyUser = getSafetyUser(user);
        //记录用户登录状态
        request.getSession().setAttribute(UserContent.USER_LOGIN_STATE, safetyUser);
        System.out.println("============");
        return ResultUtils.success(safetyUser);
    }

    /**
     * 对user脱敏
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user){
        if(user == null) throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录失败");
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        return safetyUser;
    }
}




