package com.flamingo.comeon.rpc.core;

import java.util.HashSet;
import java.util.Set;

public class ServiceProviderMediator {
    TransporterFactory transporterFactory;

    public void init(ServiceLoader serviceLoader) {
        Set<ServiceProvider> providers = serviceLoader.loadServiceProviders();
        Set<Transporter> transporters = new HashSet<>();
        for (ServiceProvider provider : providers) {
            transporters.add(transporterFactory.newTransport(provider.getConfig()));
        }
    }
}
