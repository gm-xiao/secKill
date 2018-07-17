package com.example.demo.common;

import java.io.Serializable;

public class ResponseVo implements Serializable {

    private Integer state;

    private String message;

    private Object data;

    public Integer getState() {
        return state;
    }

    public ResponseVo setState(Integer state) {
        this.state = state;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseVo setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseVo setData(Object data) {
        this.data = data;
        return this;
    }
}
