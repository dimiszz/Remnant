package servidor;

public class Resultado<T> {
    private String code;
    private T value;
    private boolean success;
    private String message;

    private Resultado(String code, T object, boolean success) {
        this.value = object;
        this.success = success;
        this.code = code;
    }

    private Resultado(String message, boolean success, String code) {
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public static <T> Resultado<T> OK(String code, T object){
        return new Resultado<>(code, object, true);
    }

    public static <T> Resultado<T> Error(String message){
        return new Resultado<>(message, false, "100");
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