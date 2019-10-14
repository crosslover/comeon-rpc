package com.flamingo.comeon.rpc.core.config;

import java.util.List;

public class Config {

    public static class ProviderConfig {
        private int port;
        private List<Class<?>> classes;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public List<Class<?>> getClasses() {
            return classes;
        }

        public void setClasses(List<Class<?>> classes) {
            this.classes = classes;
        }
    }

    public static class ConsumerConfig {
        private String ip;
        private int port;
        private List<Class<?>> classes;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public List<Class<?>> getClasses() {
            return classes;
        }

        public void setClasses(List<Class<?>> classes) {
            this.classes = classes;
        }
    }

    private ProviderConfig providerConfig;
    private ConsumerConfig consumerConfig;

    public ProviderConfig getProviderConfig() {
        return providerConfig;
    }

    public void setProviderConfig(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }
}
