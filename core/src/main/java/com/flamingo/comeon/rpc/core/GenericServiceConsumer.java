package com.flamingo.comeon.rpc.core;

public class GenericServiceConsumer implements ServiceConsumer {
    ServiceConsumerConfig config;

    public GenericServiceConsumer(ServiceConsumerConfig config) {
        this.config = config;
    }

    @Override
    public ServiceConsumerConfig getConfig() {
        return config;
    }
}
