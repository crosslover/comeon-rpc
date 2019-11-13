package com.flamingo.comeon.rpc.core;

public class IPPortServiceProviderConfig implements ServiceProviderConfig {
    private int port;

    public IPPortServiceProviderConfig(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
