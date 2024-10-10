package message;

public class Response<T> {
    public String code;
    public T responseObject;

    public Response(String code, T responseObject){
        this.code = code;
        this.responseObject = responseObject;
    }
}
