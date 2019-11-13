package com.flamingo.comeon.rpc.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ServiceProviderMediator {
    Map<Class<?>, ServiceProvider> serviceImplementerRegistry = new HashMap<>();
    Map<Class<? extends ServiceConsumer>, Transporter> transporterRegistry;
    Map<Call, Object> waitingCalls;
    ExecutorService remoteCallExecutor;

    public Object getReturnForCall(Call call) {
        Method method = call.getMethod();
        Class<? extends ServiceProvider> interfaceClass = (Class<? extends ServiceProvider>)method.getDeclaringClass();
        ServiceProvider serviceProvider = serviceImplementerRegistry.get(interfaceClass);
        try {
            return method.invoke(serviceProvider, call.getArgs());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerServiceProvider(Class<?> clazz, ServiceProvider serviceProvider) {
        serviceImplementerRegistry.put(clazz, serviceProvider);
    }
}
