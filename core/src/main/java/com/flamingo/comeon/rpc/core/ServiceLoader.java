package com.flamingo.comeon.rpc.core;

import java.util.Set;

public interface ServiceLoader {
    Set<ServiceConsumer> loadServiceConsumers();
    Set<ServiceProvider> loadServiceProviders();
}
