package server;

public class Result<T> {
    private String code;
    private T value;
    private boolean success;
    private String message;

    private Result(String code, T object, boolean success) {
        this.value = object;
        this.success = success;
        this.code = code;
    }

    private Result(String message, boolean success, String code) {
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public static <T> Result<T> OK(String code, T object){
        return new Result<>(code, object, true);
    }

    public static <T> Result<T> Error(String message){
        return new Result<>(message, false, "100");
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}