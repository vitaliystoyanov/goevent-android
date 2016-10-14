package com.stoyanov.developer.goevent.mvp.model.domain;

public final class SuccessLogout {
    private String logout;

    public SuccessLogout(String logout) {
        this.logout = logout;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }
}
