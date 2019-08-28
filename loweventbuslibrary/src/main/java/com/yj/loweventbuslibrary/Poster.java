package com.yj.loweventbuslibrary;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Poster implements Serializable {
    private Object register;// 订阅者
    private Object event;// 发送事件
    private Method method;//  方法


    public Poster(Object register, Object event, Method method) {
        this.register = register;
        this.event = event;
        this.method = method;
    }

    public Object getRegister() {
        return register;
    }

    public Object getEvent() {
        return event;
    }

    public Method getMethod() {
        return method;
    }
}
