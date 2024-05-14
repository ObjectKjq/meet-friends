package com.kjq.project.model.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建用户请求体
 *
 * @author
 * @from
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 0普通用户-1管理员
     */
    private Integer userRole;

    /**
     * 用户标签列表
     */
    private String tags;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 上传的文件
     */
    private MultipartFile avatarFile;
}
