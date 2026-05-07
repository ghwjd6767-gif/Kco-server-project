package com.sparta.kcoserverproject.global.common;

public record ApiResponse<T> (
        boolean success,
        T data,
        String message
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "요청 성공");
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, "요청 성공");
    }

    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
