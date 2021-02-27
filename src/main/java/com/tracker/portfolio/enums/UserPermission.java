package com.tracker.portfolio.enums;

import com.google.common.collect.Sets;

import java.util.Set;

public enum UserPermission {
    STOCK_READ("stock:read"),
    STOCK_WRITE("stock:write"),
    POSITION_READ("position:read"),
    POSITION_WRITE("position:write"),
    PORTFOLIO_READ("portfolio:read"),
    PORTFOLIO_WRITE("portfolio:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public static Set<UserPermission> getAdminPermissionSet() {
        return Sets.newHashSet(UserPermission.values());
    }

    public static Set<UserPermission> getUserPermissionSet() {
        return Sets.newHashSet(STOCK_READ, POSITION_READ, POSITION_WRITE, PORTFOLIO_READ, PORTFOLIO_WRITE);
    }
}
