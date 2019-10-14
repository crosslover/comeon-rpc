package com.flamingo.comeon.rpc.core;

public class IPPortServiceConsumerConfig implements ServiceConsumerConfig {
    private String ip;
    private int port;

    public IPPortServiceConsumerConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
