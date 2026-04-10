package com.hatelife.hrms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hatelife.hrms.entity.User;
import com.hatelife.hrms.enums.UserStatusEnum;
import com.hatelife.hrms.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 用户管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManageService {

    private final UserMapper userMapper;

    /**
     * 分页查询用户列表
     */
    public Page<User> listUsers(int current, int size, String keyword, String department) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .like(StrUtil.isNotBlank(keyword), User::getName, keyword)
                .or(StrUtil.isNotBlank(keyword), w -> w.like(User::getPhone, keyword))
                .eq(StrUtil.isNotBlank(department), User::getDepartment, department)
                .orderByDesc(User::getCreatedAt);
        return userMapper.selectPage(new Page<>(current, size), wrapper);
    }

    /**
     * 创建用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(User user) {
        assert StrUtil.isNotBlank(user.getUsername()) : "用户名不能为空";
        assert StrUtil.isNotBlank(user.getPassword()) : "密码不能为空";
        assert StrUtil.isNotBlank(user.getName()) : "姓名不能为空";
        assert StrUtil.isNotBlank(user.getPhone()) : "手机号不能为空";

        assert userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername())
        ) == 0 : "用户名已存在";
        assert userMapper.selectCount(
                Wrappers.<User>lambdaQuery().eq(User::getPhone, user.getPhone())
        ) == 0 : "手机号已被注册";

        user.setIsAdmin(Objects.isNull(user.getIsAdmin()) ? 0 : user.getIsAdmin());
        user.setStatus(Objects.isNull(user.getStatus()) ? UserStatusEnum.ENABLED.getCode() : user.getStatus());

        userMapper.insert(user);
        log.info("创建用户成功：userId={}, username={}, name={}", user.getId(), user.getUsername(), user.getName());
    }

    /**
     * 更新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        assert Objects.nonNull(user.getId()) : "用户ID不能为空";

        User existing = userMapper.selectById(user.getId());
        assert Objects.nonNull(existing) : "用户不存在";

        userMapper.updateById(user);
        log.info("更新用户成功：userId={}", user.getId());
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        assert Objects.nonNull(userId) : "用户ID不能为空";

        User existing = userMapper.selectById(userId);
        assert Objects.nonNull(existing) : "用户不存在";
        assert existing.getIsAdmin() != 1 : "不允许删除管理员账号";

        userMapper.deleteById(userId);
        log.info("删除用户成功：userId={}", userId);
    }
}
