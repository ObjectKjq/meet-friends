package com.kjq.project.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.kjq.project.annotation.AuthCheck;
import com.kjq.project.common.ErrorCode;
import com.kjq.project.constant.UserConstant;
import com.kjq.project.exception.BusinessException;
import com.kjq.project.model.entity.User;
import com.kjq.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限校验 AOP
 *
 * @author yupi
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User user = userService.getLoginUser(request);
        // 拥有任意权限即通过
        if (CollectionUtils.isNotEmpty(anyRole)) {
            String userRole = user.getUserRole() == UserConstant.DEFAULT_ROLE ? "user" : "admin";
            if (!anyRole.contains(userRole)) {
                throw new BusinessException(ErrorCode.NOT_AUTH, "用户没有权限访问!!");
            }
        }
        // 必须有所有权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            String userRole = user.getUserRole() == UserConstant.DEFAULT_ROLE ? "user" : "admin";
            if (!mustRole.equals(userRole)) {
                throw new BusinessException(ErrorCode.NOT_AUTH, "用户没有权限访问!!");
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

