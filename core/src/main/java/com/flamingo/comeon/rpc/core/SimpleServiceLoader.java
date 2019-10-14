package com.flamingo.comeon.rpc.core;

import java.util.Set;

public class SimpleServiceLoader implements ServiceLoader {

    private Set<ServiceConsumer> consumers;
    private Set<ServiceProvider> providers;

    public SimpleServiceLoader(Set<ServiceConsumer> consumers, Set<ServiceProvider> providers) {
        this.consumers = consumers;
        this.providers = providers;
        validate();
    }

    @Override
    public Set<ServiceConsumer> loadServiceConsumers() {
        return consumers;
    }

    @Override
    public Set<ServiceProvider> loadServiceProviders() {
        return providers;
    }

    // TODO
    private void validate() {
    }
}
