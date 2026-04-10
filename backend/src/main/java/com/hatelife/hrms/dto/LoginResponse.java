package com.hatelife.hrms.dto;

import lombok.Data;

/**
 * 登录响应 DTO
 */
@Data
public class LoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 是否管理员
     */
    private Boolean isAdmin;

    /**
     * Session Token
     */
    private String token;

    public static LoginResponse build(com.hatelife.hrms.entity.User user, String token) {
        LoginResponse resp = new LoginResponse();
        resp.setUserId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setName(user.getName());
        resp.setIsAdmin(user.getIsAdmin() == 1);
        resp.setToken(token);
        return resp;
    }
}
