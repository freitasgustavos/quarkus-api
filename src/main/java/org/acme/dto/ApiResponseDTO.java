package org.acme.dto;

public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "Operation successful", data);
    }

    public static ApiResponseDTO<Object> error(String message, Object data) {
        return new ApiResponseDTO<>(false, message, data);
    }

    public static ApiResponseDTO<Object> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }
}