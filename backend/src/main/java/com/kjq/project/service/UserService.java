package com.kjq.project.service;

import com.kjq.project.common.BaseResponse;
import com.kjq.project.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* @author 86175
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-03-11 00:44:20
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 登录接口
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户信息
     */
    BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取用户对象
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户脱密
     * @param user
     * @return
     */
    User getSafetyUser(User user);


    /**
     * 内存过滤
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);

    /**
     * sql过滤
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTagsSQL(List<String> tagNameList);

    /**
     * 修改用户信息
     * @return
     */
    Integer updateUser(User user, User loginUser);

    boolean isAdmin(HttpServletRequest request);

    List<User> matchUsers(long num, User loginUser);

    Map<Long, List<User>> matchListUsers(long num, List<User> users);

    List<User> match(User loginUser, List<User> userList, long num);
}
