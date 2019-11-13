package com.flamingo.comeon.rpc.core;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

public class Call implements Serializable {
    private Method method;

    private Object[] args;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Call(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public static Serializable toSerializable(Call call) {
        return new ProxyObject(call.method.getDeclaringClass().getName(), call.getMethod().getName(), call.args);
    }

    public static Call fromSerializable(Serializable o) {
        ProxyObject po = (ProxyObject) o;
        try {
            Class clazz = Class.forName(po.interfaceName);
            Method method = clazz.getMethod(po.methodName);
            return new Call(method, po.args);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ProxyObject implements Serializable {
        String interfaceName;
        String methodName;
        Object[] args;

        public ProxyObject(String interfaceName, String methodName, Object[] args) {
            this.interfaceName = interfaceName;
            this.methodName = methodName;
            this.args = args;
        }
    }
}
