package com.hatelife.hrms.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hatelife.hrms.entity.Okr;
import com.hatelife.hrms.entity.OkrReviewLog;
import com.hatelife.hrms.entity.UserRelation;
import com.hatelife.hrms.enums.OkrStatusEnum;
import com.hatelife.hrms.enums.ReviewActionEnum;
import com.hatelife.hrms.mapper.OkrMapper;
import com.hatelife.hrms.mapper.OkrReviewLogMapper;
import com.hatelife.hrms.mapper.UserRelationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * OKR 管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OkrService {

    private final OkrMapper okrMapper;
    private final OkrReviewLogMapper reviewLogMapper;
    private final UserRelationMapper userRelationMapper;

    /**
     * 查询用户自己的 OKR 列表
     */
    public Page<Okr> listMyOkr(Long userId, int current, int size, Integer status) {
        LambdaQueryWrapper<Okr> wrapper = Wrappers.<Okr>lambdaQuery()
                .eq(Okr::getUserId, userId)
                .eq(Objects.nonNull(status), Okr::getStatus, status)
                .orderByDesc(Okr::getCreatedAt);
        return okrMapper.selectPage(new Page<>(current, size), wrapper);
    }

    /**
     * 新增 OKR
     */
    @Transactional(rollbackFor = Exception.class)
    public Okr createOkr(Long userId, Okr okr) {
        // 检查是否有审核中的 OKR（有则不能新增）
        long reviewingCount = okrMapper.selectCount(
                Wrappers.<Okr>lambdaQuery()
                        .eq(Okr::getUserId, userId)
                        .eq(Okr::getStatus, OkrStatusEnum.REVIEWING.getCode())
        );
        assert reviewingCount == 0 : "当前有审核中的OKR，无法新增";

        okr.setUserId(userId);
        okr.setStatus(OkrStatusEnum.REVIEWING.getCode());
        okrMapper.insert(okr);

        log.info("创建OKR成功：okrId={}, userId={}", okr.getId(), userId);
        return okr;
    }

    /**
     * 编辑 OKR
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOkr(Long userId, Long okrId, Okr okr) {
        Okr existing = okrMapper.selectById(okrId);
        assert Objects.nonNull(existing) : "OKR不存在";
        assert existing.getUserId().equals(userId) : "无权操作该OKR";
        assert !Objects.equals(existing.getStatus(), OkrStatusEnum.REVIEWING.getCode()) : "审核中的OKR不可编辑";

        okr.setId(okrId);
        okr.setUserId(userId);
        okr.setStatus(OkrStatusEnum.REVIEWING.getCode());
        okr.setReviewReason(null);
        okrMapper.updateById(okr);

        log.info("编辑OKR成功：okrId={}", okrId);
    }

    /**
     * 提交审核（审核通过/驳回/作废）
     */
    @Transactional(rollbackFor = Exception.class)
    public void reviewOkr(Long reviewerId, Long okrId, Integer action, String reason) {
        Okr okr = okrMapper.selectById(okrId);
        assert Objects.nonNull(okr) : "OKR不存在";
        assert Objects.equals(okr.getStatus(), OkrStatusEnum.REVIEWING.getCode()) : "仅审核中的OKR可操作";

        if (Objects.equals(action, ReviewActionEnum.APPROVE.getCode())) {
            okr.setStatus(OkrStatusEnum.NORMAL.getCode());
            okr.setReviewReason(null);
        } else if (Objects.equals(action, ReviewActionEnum.REJECT.getCode())) {
            assert StrUtil.isNotBlank(reason) : "驳回必须填写原因";
            okr.setStatus(OkrStatusEnum.REJECTED.getCode());
            okr.setReviewReason(reason);
        } else if (Objects.equals(action, ReviewActionEnum.VOID.getCode())) {
            okr.setStatus(OkrStatusEnum.VOID.getCode());
        }

        okrMapper.updateById(okr);

        // 记录审核日志
        OkrReviewLog reviewLog = new OkrReviewLog();
        reviewLog.setOkrId(okrId);
        reviewLog.setReviewerId(reviewerId);
        reviewLog.setAction(action);
        reviewLog.setReason(reason);
        reviewLogMapper.insert(reviewLog);

        log.info("审核OKR：okrId={}, reviewerId={}, action={}, reason={}", okrId, reviewerId, action, reason);
    }

    /**
     * 查询团队 OKR（管理员：全部人；领导：其下级）
     */
    public Page<Okr> listTeamOkr(Long userId, boolean isAdmin, int current, int size, Integer status, Long subordinateId) {
        LambdaQueryWrapper<Okr> wrapper = Wrappers.<Okr>lambdaQuery()
                .eq(Objects.nonNull(status), Okr::getStatus, status)
                .orderByDesc(Okr::getCreatedAt);

        if (!isAdmin) {
            // 领导只看下级的 OKR
            if (Objects.nonNull(subordinateId)) {
                wrapper.eq(Okr::getUserId, subordinateId);
            } else {
                var subordinates = userRelationMapper.selectList(
                        Wrappers.<UserRelation>lambdaQuery()
                                .eq(UserRelation::getSuperiorId, userId)
                                .select(UserRelation::getSubordinateId)
                );
                var subIds = subordinates.stream().map(UserRelation::getSubordinateId).toList();
                if (subIds.isEmpty()) {
                    return new Page<>(current, size, 0);
                }
                wrapper.in(Okr::getUserId, subIds);
            }
        }

        return okrMapper.selectPage(new Page<>(current, size), wrapper);
    }
}
