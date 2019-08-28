package com.yj.loweventbuslibrary;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LowEventBus {

    private static List<Object> registers = new ArrayList<>();// 保存订阅者
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static final String CONSTANT_POSTER = "CONSTANT_POSTER";
    private static Handler mainHandle = new MainHandle();



    private static class MainHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Poster poster = (Poster) msg.getData().get(CONSTANT_POSTER);
            Method method = poster.getMethod();
            Object register = poster.getRegister();
            Object event = poster.getEvent();
            invoke(method, register, event);
        }
    }


    /**
     * 订阅者订阅
     * @param register
     */
    public static void register(Object register) {
        registers.add(register);
    }

    /**
     * 订阅者解除订阅
     * @param register
     */
    public static void unRegister(Object register) {
        registers.remove(register);
    }

    /**
     * 发送事件
     * @param event
     */
    public static void post(final Object event) {
        for (final Object register : registers) { // 遍历订阅者
            Class c = register.getClass(); // 获取订阅者的类对象
            Method[] methods = c.getMethods();// 获取类对象里的所有方法
            for (final Method method : methods) { // 遍历方法
                if (method.isAnnotationPresent(Subscribe.class)) { // 判断方法是否有Subscribe.class注解
                    Class[] parameterTypes =  method.getParameterTypes(); // 获取方法里的所有入参类型
                    for (Class parameterType : parameterTypes) { // 遍历入参类型
                        if (parameterType.getName().equals(event.getClass().getName())) { // 判断事件是否和入参类型相同
                                // 切换线程
                                ThreadMode threadMode = method.getAnnotation(Subscribe.class).threadMode();
                                Poster poster = new Poster(register, event, method);
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(CONSTANT_POSTER, poster);
                                message.setData(bundle);
                                switch (threadMode) {
                                    case MAIN:
                                        mainHandle.sendMessage(message);
                                        break;
                                    case BACKGROUND:
                                        executor.submit(new Runnable() {
                                            @Override
                                            public void run() {
                                                invoke(method, register, event);
                                            }
                                        });
                                        break;
                                }
                        }
                    }
                }
            }
        }
    }

    // 利用反射
    private static void invoke(Method method, Object register, Object event) {
        try {
            method.invoke(register, event);// 执行方法
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
