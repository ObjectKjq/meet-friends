package com.kjq.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kjq.project.common.BaseResponse;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.common.ResultUtils;
import com.kjq.project.constant.UserConstant;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.mapper.TagMapper;
import com.kjq.project.model.entity.Tag;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.UserService;
import com.kjq.project.mapper.UserMapper;
import com.kjq.project.utils.AlgorithmUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private TagMapper tagMapper;

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
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return currentUser;
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
        safetyUser.setTags(user.getTags());
        safetyUser.setProfile(user.getProfile());
        return safetyUser;
    }

    /**
     * 根据标签搜索用户,内存过滤
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        //在内容中运算是否包含要求的表亲啊，更灵活
        Gson gson = new Gson();
        return users.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)){
                return false;
            }
            Set<String> tempNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            //如果tempNameSet为空，就赋值空对象
            tempNameSet = Optional.ofNullable(tempNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if(!tempNameSet.contains(tagName)){
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 根据标签搜索用户,SQL过滤
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUserByTagsSQL(List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //直接and查询
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 修改用户信息
     * @return
     */
    @Override
    public Integer updateUser(User user, User loginUser) {
        if(user == null || loginUser == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //验证权限，仅管理员和自己可以修改
        if (!Objects.equals(user.getId(), loginUser.getId())
                && loginUser.getUserRole() != UserConstant.ADMIN_ROLE){
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        user.setId(loginUser.getId());
        // todo 这里需要检查用户发送的信息是否合法
        //检查用户上传的标签是否合法
        if(user.getTags() != null){
            Gson gson = new Gson();
            List<String> tagList = gson.fromJson(user.getTags(), new TypeToken<List<String>>(){}.getType());
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("tagName", tagList);
            List<Tag> users = tagMapper.selectList(queryWrapper);
            if(users.size() != tagList.size()){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        //修改用户信息
        return userMapper.updateById(user);
    }

    /**
     * 判断是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        //仅管理员可以查询
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) attribute;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    /**
     * 根据编辑距离算法，算出与自已标签相似的用户
     * @param num
     * @param loginUser
     * @return
     */
    @Override
    public List<User> matchUsers(long num, User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        /**
         * todo 这里不能查询所有用户
         * 可以采用大数据推荐
         * 1.检索：尽可能地查询符合要求的数据（比如按记录查询）
         * 2.召回：查询可能用到的数据
         * 3.粗排：粗略排序，简单地运算（相对轻量）
         * 4.精排：精细排序，确定固定排位
         */
        //查出所有的用户数据
        List<User> userList = this.list(queryWrapper);
        return match(loginUser, userList, num);
    }

    /**
     * 根据缓存中查到的所有用户，查询他们的缓存
     * @param num
     * @param users
     * @return
     */
    @Override
    public Map<Long, List<User>> matchListUsers(long num, List<User> users) {
        if (users.isEmpty()){
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        //查出所有的用户数据
        List<User> userList = this.list(queryWrapper);
        //存储所有用户的数据
        Map<Long, List<User>> userNumMap = new TreeMap<>();
        // List<List<User>> userNumList = new ArrayList<>();
        for (User loginUser : users) {
            List<User> finalUserList = match(loginUser, userList, num);
            //查询所有的用户
            userNumMap.put(loginUser.getId(), finalUserList);
        }
        return userNumMap;
    }

    /**
     * 查询算法抽离
     */
    @Override
    public List<User> match(User loginUser, List<User> userList, long num){
        String tags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {}.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {}.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        // 原本顺序的 userId 列表，获取拍好序的对象userId到数组中
        List<Long> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper)
                .stream()
                .map(user -> getSafetyUser(user))
                .collect(Collectors.groupingBy(User::getId));
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }
}




