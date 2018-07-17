package com.example.demo.common;

import java.io.Serializable;
import java.util.Map;

public class QueueTemplate implements Serializable {

    private String method;

    private Object data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
