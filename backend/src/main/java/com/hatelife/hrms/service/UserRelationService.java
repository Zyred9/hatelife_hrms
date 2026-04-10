package com.hatelife.hrms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hatelife.hrms.entity.UserRelation;
import com.hatelife.hrms.mapper.UserRelationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 用户关系管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRelationService {

    private final UserRelationMapper userRelationMapper;

    /**
     * 建立上下级关系
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRelation(Long superiorId, Long subordinateId, Integer relationType) {
        assert Objects.nonNull(superiorId) : "上级ID不能为空";
        assert Objects.nonNull(subordinateId) : "下级ID不能为空";
        assert !superiorId.equals(subordinateId) : "不能建立自身关系";

        long exists = userRelationMapper.selectCount(
                Wrappers.<UserRelation>lambdaQuery()
                        .eq(UserRelation::getSuperiorId, superiorId)
                        .eq(UserRelation::getSubordinateId, subordinateId)
        );
        assert exists == 0 : "该关系已存在";

        UserRelation relation = new UserRelation();
        relation.setSuperiorId(superiorId);
        relation.setSubordinateId(subordinateId);
        relation.setRelationType(relationType);
        userRelationMapper.insert(relation);

        log.info("建立上下级关系：superiorId={}, subordinateId={}, type={}", superiorId, subordinateId, relationType);
    }

    /**
     * 解除上下级关系
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRelation(Long superiorId, Long subordinateId) {
        userRelationMapper.delete(
                Wrappers.<UserRelation>lambdaQuery()
                        .eq(UserRelation::getSuperiorId, superiorId)
                        .eq(UserRelation::getSubordinateId, subordinateId)
        );
        log.info("解除上下级关系：superiorId={}, subordinateId={}", superiorId, subordinateId);
    }

    /**
     * 查询所有关系
     */
    public List<UserRelation> listAllRelations() {
        return userRelationMapper.selectList(
                Wrappers.<UserRelation>lambdaQuery()
                        .orderByAsc(UserRelation::getSuperiorId)
        );
    }

    /**
     * 查询指定用户的下级
     */
    public List<UserRelation> listSubordinates(Long superiorId) {
        return userRelationMapper.selectList(
                Wrappers.<UserRelation>lambdaQuery()
                        .eq(UserRelation::getSuperiorId, superiorId)
        );
    }

    /**
     * 查询用户是否有下级（判断是否为"领导"身份）
     */
    public boolean hasSubordinate(Long userId) {
        return userRelationMapper.selectCount(
                Wrappers.<UserRelation>lambdaQuery()
                        .eq(UserRelation::getSuperiorId, userId)
        ) > 0;
    }
}
