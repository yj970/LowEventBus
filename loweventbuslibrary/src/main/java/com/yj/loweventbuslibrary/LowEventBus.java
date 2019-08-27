package com.yj.loweventbuslibrary;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LowEventBus {
    /**
     * 保存订阅者
     */
    private static List<Object> registers = new ArrayList<>();

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
    public static void post(Object event) {
        for (Object register : registers) { // 遍历订阅者
            Class c = register.getClass(); // 获取订阅者的类对象
            Method[] methods = c.getMethods();// 获取类对象里的所有方法
            for (Method method : methods) { // 遍历方法
                if (method.isAnnotationPresent(Subscribe.class)) { // 判断方法是否有Subscribe.class注解
                    Class[] parameterTypes =  method.getParameterTypes(); // 获取方法里的所有入参类型
                    for (Class parameterType : parameterTypes) { // 遍历入参类型
                        if (parameterType.getName().equals(event.getClass().getName())) { // 判断事件是否和入参类型相同
                            try {
                                method.invoke(register, event);// 执行方法
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("IllegalAccessException "+e.getMessage());
                            } catch (InvocationTargetException e) {
                                throw new RuntimeException("InvocationTargetException "+e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
}
