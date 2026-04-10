package com.hatelife.hrms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hatelife.hrms.dto.R;
import com.hatelife.hrms.entity.User;
import com.hatelife.hrms.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 账号管理控制器（仅管理员）
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserManageController {

    private final UserManageService userManageService;

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public R<Page<User>> list(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department) {
        return R.ok(userManageService.listUsers(current, size, keyword, department));
    }

    /**
     * 创建用户
     */
    @PostMapping
    public R<Void> create(@RequestBody User user) {
        userManageService.createUser(user);
        return R.ok();
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userManageService.updateUser(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userManageService.deleteUser(id);
        return R.ok();
    }
}
