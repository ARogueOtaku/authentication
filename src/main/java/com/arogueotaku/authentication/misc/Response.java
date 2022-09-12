package com.arogueotaku.authentication.misc;

@SuppressWarnings("unused")
public class Response<T> {

    private String errorMessage;
    private boolean error;
    private T data;
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public Response<T> error(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
        return this;
    }

    public Response<T> data(T data) {
        this.error = false;
        this.data = data;
        return this;
    }
}
