package com.hatelife.hrms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hatelife.hrms.entity.AssistantQa;
import com.hatelife.hrms.entity.NotificationConfig;
import com.hatelife.hrms.mapper.AssistantQaMapper;
import com.hatelife.hrms.mapper.NotificationConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 系统设置服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final NotificationConfigMapper notificationConfigMapper;
    private final AssistantQaMapper assistantQaMapper;

    /**
     * 查询所有通知配置
     */
    public List<NotificationConfig> listNotificationConfigs() {
        return notificationConfigMapper.selectList(
                Wrappers.<NotificationConfig>lambdaQuery()
                        .orderByAsc(NotificationConfig::getConfigKey)
        );
    }

    /**
     * 更新通知配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNotificationConfig(NotificationConfig config) {
        assert Objects.nonNull(config.getId()) : "配置ID不能为空";
        notificationConfigMapper.updateById(config);
        log.info("更新通知配置成功：configId={}, enabled={}", config.getId(), config.getEnabled());
    }

    /**
     * 查询助手问题库
     */
    public List<AssistantQa> listAssistantQa(Integer assistantType) {
        return assistantQaMapper.selectList(
                Wrappers.<AssistantQa>lambdaQuery()
                        .eq(Objects.nonNull(assistantType), AssistantQa::getAssistantType, assistantType)
                        .eq(AssistantQa::getEnabled, 1)
                        .orderByAsc(AssistantQa::getSortOrder)
        );
    }

    /**
     * 新增助手问题
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAssistantQa(AssistantQa qa) {
        assistantQaMapper.insert(qa);
        log.info("新增助手问题：qaId={}, question={}", qa.getId(), qa.getQuestion());
    }

    /**
     * 更新助手问题
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAssistantQa(AssistantQa qa) {
        assert Objects.nonNull(qa.getId()) : "问题ID不能为空";
        assistantQaMapper.updateById(qa);
        log.info("更新助手问题：qaId={}", qa.getId());
    }

    /**
     * 删除助手问题
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssistantQa(Long qaId) {
        assistantQaMapper.deleteById(qaId);
        log.info("删除助手问题：qaId={}", qaId);
    }
}
