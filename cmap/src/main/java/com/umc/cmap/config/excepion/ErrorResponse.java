package com.umc.cmap.config.excepion;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;

    public ErrorResponse(RuntimeException e) {
        this.message = e.getMessage();
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
