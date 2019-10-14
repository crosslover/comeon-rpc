package com.flamingo.comeon.rpc.core;

public interface TransporterFactory {
    Transporter newTransport(ServiceConsumerConfig consumerConfig);

    Transporter newTransport(ServiceProviderConfig providerConfig);
}
