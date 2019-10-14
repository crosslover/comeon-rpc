package com.flamingo.comeon.rpc.core;

public class GenericServiceProvider implements ServiceProvider {
    ServiceProviderConfig config;

    public GenericServiceProvider(ServiceProviderConfig config) {
        this.config = config;
    }

    @Override
    public ServiceProviderConfig getConfig() {
        return config;
    }
}
