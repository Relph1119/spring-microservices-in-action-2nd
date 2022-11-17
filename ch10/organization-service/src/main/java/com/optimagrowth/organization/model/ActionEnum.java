package com.optimagrowth.organization.model;

public enum ActionEnum {
    GET("GET"), CREATED("CREATED"), UPDATED("UPDATED"), DELETED("DELETED");

    private final String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}


