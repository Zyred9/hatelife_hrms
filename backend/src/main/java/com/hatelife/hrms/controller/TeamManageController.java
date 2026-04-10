package com.hatelife.hrms.controller;

import com.hatelife.hrms.dto.R;
import com.hatelife.hrms.entity.UserRelation;
import com.hatelife.hrms.service.UserRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 团队管理控制器（组织架构，仅管理员）
 */
@RestController
@RequestMapping("/api/admin/team")
@RequiredArgsConstructor
public class TeamManageController {

    private final UserRelationService userRelationService;

    /**
     * 查询所有关系（画布用）
     */
    @GetMapping("/relations")
    public R<List<UserRelation>> listRelations() {
        return R.ok(userRelationService.listAllRelations());
    }

    /**
     * 建立上下级关系
     */
    @PostMapping("/relation")
    public R<Void> addRelation(
            @RequestParam Long superiorId,
            @RequestParam Long subordinateId,
            @RequestParam(defaultValue = "1") Integer relationType) {
        userRelationService.addRelation(superiorId, subordinateId, relationType);
        return R.ok();
    }

    /**
     * 解除上下级关系
     */
    @DeleteMapping("/relation")
    public R<Void> removeRelation(
            @RequestParam Long superiorId,
            @RequestParam Long subordinateId) {
        userRelationService.removeRelation(superiorId, subordinateId);
        return R.ok();
    }

    /**
     * 查询指定用户的下级
     */
    @GetMapping("/subordinates/{userId}")
    public R<List<UserRelation>> listSubordinates(@PathVariable Long userId) {
        return R.ok(userRelationService.listSubordinates(userId));
    }
}
