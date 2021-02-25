package com.tracker.portfolio.enums;

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
}
