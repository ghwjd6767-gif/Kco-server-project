package com.sparta.kcoserverproject.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    // Point
    POINT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    POINT_INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "충전 금액은 0원보다 커야 합니다."),

    // Redis
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "현재 주문 요청이 많습니다. 잠시 후 다시 시도해주세요."),

    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
