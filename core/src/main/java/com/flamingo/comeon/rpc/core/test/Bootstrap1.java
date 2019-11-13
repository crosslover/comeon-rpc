package com.flamingo.comeon.rpc.core.test;

import com.flamingo.comeon.rpc.core.Call;
import com.flamingo.comeon.rpc.core.HessianSerializableFactory;
import com.flamingo.comeon.rpc.core.IPPortServiceConsumerConfig;
import com.flamingo.comeon.rpc.core.JDKProxyFactory;
import com.flamingo.comeon.rpc.core.ProxyFactory;
import com.flamingo.comeon.rpc.core.ServiceConsumerMediator;
import com.flamingo.comeon.rpc.core.ServiceProviderMediator;
import com.flamingo.comeon.rpc.core.netty.NettyConsumerTransporter;
import com.flamingo.comeon.rpc.core.netty.NettyProviderTransporter;

import java.util.Objects;

public class Bootstrap1 {

    public static void main(String[] args) {
//        HessianSerializableFactory factory = new HessianSerializableFactory();
//        Call.ProxyObject po = new Call.ProxyObject("aaa", "bbb", null);
//        byte[] bytes = factory.serialize(po);
//        Object o = factory.deserialize(bytes);
//        System.out.println();
        startServer();
//        startClient();
    }

    private static void startClient() {
        ServiceConsumerMediator scm = new ServiceConsumerMediator();
        ProxyFactory proxyFactory = new JDKProxyFactory();
        AServiceConsumer proxy = proxyFactory.newProxy(AServiceConsumer.class, scm, new IPPortServiceConsumerConfig("127.0.0.1", 8620));
        NettyConsumerTransporter ct = new NettyConsumerTransporter(proxy.getConfig());
        ct.setServiceConsumerMediator(scm);
        scm.registerTransporter(AServiceConsumer.class, ct);
        ct.start();
        String result = proxy.test();
        System.out.println(result);
    }

    private static void startServer() {
        AServiceImpl service = new AServiceImpl();
        ServiceProviderMediator spm = new ServiceProviderMediator();
        spm.registerServiceProvider(AService.class, service);
        NettyProviderTransporter pt = new NettyProviderTransporter(service.getConfig());
        pt.setServiceProviderMediator(spm);
        pt.start();
    }
}
