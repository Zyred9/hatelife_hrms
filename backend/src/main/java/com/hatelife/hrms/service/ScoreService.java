package com.hatelife.hrms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hatelife.hrms.entity.ScoreRecord;
import com.hatelife.hrms.entity.ScoreRule;
import com.hatelife.hrms.enums.OkrStatusEnum;
import com.hatelife.hrms.mapper.OkrMapper;
import com.hatelife.hrms.mapper.ScoreRecordMapper;
import com.hatelife.hrms.mapper.ScoreRuleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * 绩效评分服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRecordMapper scoreRecordMapper;
    private final ScoreRuleMapper scoreRuleMapper;
    private final OkrMapper okrMapper;

    /**
     * 提交评分（自评/领导评分/最终评分）
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitScore(Long reviewerId, Long okrId, Integer reviewType, String dimensionScores) {
        // 检查 OKR 状态
        var okr = okrMapper.selectById(okrId);
        assert Objects.nonNull(okr) : "OKR不存在";
        assert Objects.equals(okr.getStatus(), OkrStatusEnum.NORMAL.getCode()) : "仅正常状态的OKR可评分";

        // 检查是否已评分
        long existingCount = scoreRecordMapper.selectCount(
                Wrappers.<ScoreRecord>lambdaQuery()
                        .eq(ScoreRecord::getOkrId, okrId)
                        .eq(ScoreRecord::getReviewerId, reviewerId)
                        .eq(ScoreRecord::getReviewType, reviewType)
        );
        assert existingCount == 0 : "该轮评分已完成，不可重复提交";

        // 计算总分
        BigDecimal totalScore = calculateTotalScore(dimensionScores);

        ScoreRecord record = new ScoreRecord();
        record.setOkrId(okrId);
        record.setReviewerId(reviewerId);
        record.setReviewType(reviewType);
        record.setDimensionScores(dimensionScores);
        record.setTotalScore(totalScore);
        record.setLocked(1);

        scoreRecordMapper.insert(record);

        log.info("提交评分成功：okrId={}, reviewerId={}, reviewType={}, totalScore={}",
                okrId, reviewerId, reviewType, totalScore);
    }

    /**
     * 查询 OKR 的所有评分记录
     */
    public List<ScoreRecord> listScoresByOkr(Long okrId) {
        return scoreRecordMapper.selectList(
                Wrappers.<ScoreRecord>lambdaQuery()
                        .eq(ScoreRecord::getOkrId, okrId)
                        .orderByAsc(ScoreRecord::getReviewType)
        );
    }

    /**
     * 查询评分规则
     */
    public List<ScoreRule> listScoreRules(Integer cycleType) {
        return scoreRuleMapper.selectList(
                Wrappers.<ScoreRule>lambdaQuery()
                        .eq(Objects.nonNull(cycleType), ScoreRule::getCycleType, cycleType)
                        .eq(ScoreRule::getEnabled, 1)
                        .orderByAsc(ScoreRule::getSortOrder)
        );
    }

    /**
     * 更新评分规则
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateScoreRule(ScoreRule rule) {
        assert Objects.nonNull(rule.getId()) : "规则ID不能为空";

        ScoreRule existing = scoreRuleMapper.selectById(rule.getId());
        assert Objects.nonNull(existing) : "评分规则不存在";

        scoreRuleMapper.updateById(rule);
        log.info("更新评分规则成功：ruleId={}", rule.getId());
    }

    /**
     * 根据维度评分明细计算总分
     */
    private BigDecimal calculateTotalScore(String dimensionScores) {
        // TODO: 解析 JSON，遍历维度，乘以权重后求和
        // 简化实现：返回 0，实际需解析 dimensionScores JSON 并计算
        return BigDecimal.ZERO;
    }
}
