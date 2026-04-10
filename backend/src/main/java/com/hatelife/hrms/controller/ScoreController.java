package com.hatelife.hrms.controller;

import com.hatelife.hrms.dto.R;
import com.hatelife.hrms.entity.ScoreRecord;
import com.hatelife.hrms.entity.ScoreRule;
import com.hatelife.hrms.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 绩效评分控制器
 */
@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * 提交评分
     */
    @PostMapping
    public R<Void> submit(
            @RequestParam Long okrId,
            @RequestParam Integer reviewType,
            @RequestParam String dimensionScores) {
        Long reviewerId = 1L; // TODO: 从 SecurityContext 获取
        scoreService.submitScore(reviewerId, okrId, reviewType, dimensionScores);
        return R.ok();
    }

    /**
     * 查询 OKR 评分记录
     */
    @GetMapping("/okr/{okrId}")
    public R<List<ScoreRecord>> listByOkr(@PathVariable Long okrId) {
        return R.ok(scoreService.listScoresByOkr(okrId));
    }

    /**
     * 查询评分规则
     */
    @GetMapping("/rules")
    public R<List<ScoreRule>> listRules(@RequestParam(required = false) Integer cycleType) {
        return R.ok(scoreService.listScoreRules(cycleType));
    }

    /**
     * 更新评分规则（仅管理员）
     */
    @PutMapping("/rules/{id}")
    public R<Void> updateRule(@PathVariable Long id, @RequestBody ScoreRule rule) {
        rule.setId(id);
        scoreService.updateScoreRule(rule);
        return R.ok();
    }
}
