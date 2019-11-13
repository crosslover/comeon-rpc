package com.flamingo.comeon.rpc.core;

public interface ProxyFactory {
    <T extends ServiceConsumer> T newProxy(Class<T> serviceConsumer, ServiceConsumerMediator mediator,
                                           ServiceConsumerConfig config);

//    <T extends ServiceProvider> ServiceProvider newProxy(T serviceProvider);
}
