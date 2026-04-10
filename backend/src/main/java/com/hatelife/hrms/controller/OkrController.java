package com.hatelife.hrms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hatelife.hrms.entity.Okr;
import com.hatelife.hrms.dto.R;
import com.hatelife.hrms.service.OkrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * OKR 管理控制器
 */
@RestController
@RequestMapping("/api/okr")
@RequiredArgsConstructor
public class OkrController {

    private final OkrService okrService;

    /**
     * 我的 OKR 列表
     */
    @GetMapping("/my")
    public R<Page<Okr>> listMy(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status) {
        Long userId = 1L; // TODO: 从 SecurityContext 获取
        return R.ok(okrService.listMyOkr(userId, current, size, status));
    }

    /**
     * 新增 OKR
     */
    @PostMapping
    public R<Okr> create(@RequestBody Okr okr) {
        Long userId = 1L; // TODO: 从 SecurityContext 获取
        return R.ok(okrService.createOkr(userId, okr));
    }

    /**
     * 编辑 OKR
     */
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody Okr okr) {
        Long userId = 1L; // TODO: 从 SecurityContext 获取
        okrService.updateOkr(userId, id, okr);
        return R.ok();
    }

    /**
     * 审核 OKR
     */
    @PostMapping("/{id}/review")
    public R<Void> review(
            @PathVariable Long id,
            @RequestParam Integer action,
            @RequestParam(required = false) String reason) {
        Long reviewerId = 1L; // TODO: 从 SecurityContext 获取
        okrService.reviewOkr(reviewerId, id, action, reason);
        return R.ok();
    }

    /**
     * 团队 OKR 列表
     */
    @GetMapping("/team")
    public R<Page<Okr>> listTeam(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long subordinateId) {
        Long userId = 1L; // TODO: 从 SecurityContext 获取
        boolean isAdmin = false; // TODO: 从 SecurityContext 获取
        return R.ok(okrService.listTeamOkr(userId, isAdmin, current, size, status, subordinateId));
    }
}
