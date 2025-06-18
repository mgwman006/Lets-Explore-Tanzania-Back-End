package letsexploretanzania.co.tz.letsexploretanzania.common.utils;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int statusCode;

    // Constructors
    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(T data,int statusCode) {
        return new ApiResponse<>(true, "Request successful", data,statusCode);
    }

    public static <T> ApiResponse<T> success(String message, T data,int statusCode) {
        return new ApiResponse<>(true, message, data,statusCode);
    }

    public static <T> ApiResponse<T> failure(String message,int statusCode) {
        return new ApiResponse<>(false, message, null,statusCode);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
