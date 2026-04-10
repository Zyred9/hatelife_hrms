package com.hatelife.hrms.controller;

import com.hatelife.hrms.dto.R;
import com.hatelife.hrms.entity.AssistantQa;
import com.hatelife.hrms.entity.NotificationConfig;
import com.hatelife.hrms.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统设置控制器（仅管理员）
 */
@RestController
@RequestMapping("/api/admin/settings")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService systemSettingService;

    // ========== 通知配置 ==========

    @GetMapping("/notifications")
    public R<List<NotificationConfig>> listNotifications() {
        return R.ok(systemSettingService.listNotificationConfigs());
    }

    @PutMapping("/notifications/{id}")
    public R<Void> updateNotification(@PathVariable Long id, @RequestBody NotificationConfig config) {
        config.setId(id);
        systemSettingService.updateNotificationConfig(config);
        return R.ok();
    }

    // ========== 助手问题库 ==========

    @GetMapping("/assistant-qa")
    public R<List<AssistantQa>> listQa(@RequestParam(required = false) Integer assistantType) {
        return R.ok(systemSettingService.listAssistantQa(assistantType));
    }

    @PostMapping("/assistant-qa")
    public R<Void> addQa(@RequestBody AssistantQa qa) {
        systemSettingService.addAssistantQa(qa);
        return R.ok();
    }

    @PutMapping("/assistant-qa/{id}")
    public R<Void> updateQa(@PathVariable Long id, @RequestBody AssistantQa qa) {
        qa.setId(id);
        systemSettingService.updateAssistantQa(qa);
        return R.ok();
    }

    @DeleteMapping("/assistant-qa/{id}")
    public R<Void> deleteQa(@PathVariable Long id) {
        systemSettingService.deleteAssistantQa(id);
        return R.ok();
    }
}
