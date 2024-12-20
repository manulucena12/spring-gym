package com.manu.responses;

public class HttpServiceResponse<T> {

    private final int statusCode;
    private final T content;
    private final String message;

    public HttpServiceResponse(int statusCode, T content, String message){
        this.statusCode = statusCode;
        this.content = content;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getContent() {
        return content;
    }

    public String getMessage(){
        return message;
    }
}
