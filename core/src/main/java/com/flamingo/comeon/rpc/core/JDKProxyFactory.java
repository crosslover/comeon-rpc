package com.flamingo.comeon.rpc.core;

import com.flamingo.comeon.rpc.core.common.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T extends ServiceConsumer> T newProxy(Class<T> serviceConsumer, ServiceConsumerMediator mediator,
                                                  ServiceConsumerConfig config) {
        Objects.requireNonNull(serviceConsumer);
//        Assert.isTrue(serviceConsumer.isAssignableFrom(ServiceConsumer.class));
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceConsumer},
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getConfig")) {
                    return config;
                }
                Set<Method> methods = new HashSet<>(Arrays.asList(method.getDeclaringClass().getDeclaredMethods()));
                if (methods.contains(method)) {
                    Call call = new Call(method, args);
                    return mediator.remoteCallSync(serviceConsumer, call);
                }
                return null;
            }
        });
        return (T)proxy;
    }

//    @Override
//    public <T extends ServiceProvider> ServiceProvider newProxy(T serviceProvider) {
//        return null;
//    }
}
