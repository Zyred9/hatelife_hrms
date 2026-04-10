package com.hatelife.hrms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hatelife.hrms.dto.LoginRequest;
import com.hatelife.hrms.dto.LoginResponse;
import com.hatelife.hrms.entity.User;
import com.hatelife.hrms.enums.UserStatusEnum;
import com.hatelife.hrms.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 认证服务
 * <p>
 * 当前使用内存 Session 存储（开发阶段），后续可替换为 Redis。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;

    /** 内存 Session 存储：token → userId */
    private final ConcurrentMap<String, Long> sessionStore = new ConcurrentHashMap<>();

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        assert StrUtil.isNotBlank(request.getUsername()) : "用户名不能为空";
        assert StrUtil.isNotBlank(request.getPassword()) : "密码不能为空";

        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, request.getUsername())
        );
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatus() == UserStatusEnum.DISABLED.getCode()) {
            throw new IllegalArgumentException("账号已被禁用");
        }

        // 生成 token
        String token = UUID.randomUUID().toString().replace("-", "");
        sessionStore.put(token, user.getId());

        log.info("用户登录成功：userId={}, username={}", user.getId(), user.getUsername());
        return LoginResponse.build(user, token);
    }

    /**
     * 根据 token 获取用户ID
     */
    public Long getUserIdByToken(String token) {
        return sessionStore.get(token);
    }

    /**
     * 登出
     */
    public void logout(String token) {
        sessionStore.remove(token);
        log.info("用户登出：token={}", token);
    }
}
