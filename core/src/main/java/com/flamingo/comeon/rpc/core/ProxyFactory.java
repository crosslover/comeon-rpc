package com.flamingo.comeon.rpc.core;

public interface ProxyFactory {
    <T extends ServiceConsumer> ServiceConsumer newProxy(T serviceConsumer);

    <T extends ServiceProvider> ServiceProvider newProxy(T serviceProvider);
}
