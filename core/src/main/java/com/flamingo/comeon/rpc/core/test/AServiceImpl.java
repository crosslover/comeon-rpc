package com.flamingo.comeon.rpc.core.test;

import com.flamingo.comeon.rpc.core.IPPortServiceProviderConfig;
import com.flamingo.comeon.rpc.core.ServiceProvider;
import com.flamingo.comeon.rpc.core.ServiceProviderConfig;

public class AServiceImpl implements ServiceProvider, AService {

    @Override
    public String test() {
        return "Hello world!";
    }

    @Override
    public ServiceProviderConfig getConfig() {
        return new IPPortServiceProviderConfig(8620);
    }
}
