package com.tiny.configuration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationUserPermissions {
    USER_UPLOAD("user:upload"),
    USER_DELETE("user:delete"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_ACTUATOR_ENDPOINTS("admin:actuator");

    private final String permission;

    public String getPermission(){
        return permission;
    }
}