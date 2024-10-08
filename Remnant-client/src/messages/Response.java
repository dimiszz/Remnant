package messages;

public class Response<T> {
    public String code;
    public T responseObject;
    public String message;

    public Response(String code, T responseObject){
        this.code = code;
        this.responseObject = responseObject;
    }

    public Response(String code, String message){
        this.code = code;
        this.message = message;
    }
}
