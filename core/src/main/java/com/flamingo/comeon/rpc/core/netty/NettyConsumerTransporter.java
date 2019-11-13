package com.flamingo.comeon.rpc.core.netty;

import com.flamingo.comeon.rpc.core.HessianSerializableFactory;
import com.flamingo.comeon.rpc.core.IPPortServiceConsumerConfig;
import com.flamingo.comeon.rpc.core.ServiceConsumerConfig;
import com.flamingo.comeon.rpc.core.ServiceConsumerMediator;
import com.flamingo.comeon.rpc.core.Transporter;
import com.flamingo.comeon.rpc.core.common.Assert;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.io.Serializable;

public class NettyConsumerTransporter implements Transporter {

    ServiceConsumerConfig serviceConsumerConfig;
    ServiceConsumerMediator serviceConsumerMediator;
    NioEventLoopGroup clientGroup;
    volatile Channel channel;

    public NettyConsumerTransporter(ServiceConsumerConfig serviceConsumerConfig) {
        Assert.isTrue(serviceConsumerConfig instanceof IPPortServiceConsumerConfig);
        this.serviceConsumerConfig = serviceConsumerConfig;
    }

    @Override
    public void start() {

        clientGroup = new NioEventLoopGroup();

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        channel = new Bootstrap()
                                .group(clientGroup)
                                .channel(NioSocketChannel.class)
                                .handler(new ChannelInitializer<Channel>() {
                                    @Override
                                    protected void initChannel(Channel channel) throws Exception {
                                        channel.pipeline()
                                                .addLast(new LoggingHandler())
//                                                .addLast(new LineBasedFrameDecoder(1024))
                                                .addLast(new SerializationHandler(new HessianSerializableFactory()))
                                                .addLast(new RemoteCallHandler(null, serviceConsumerMediator));
                                    }
                                })
                                .connect(((IPPortServiceConsumerConfig) serviceConsumerConfig).getIp(),
                                        ((IPPortServiceConsumerConfig) serviceConsumerConfig).getPort())
                                .sync().channel();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }));
    }

    @Override
    public void send(Serializable serializable) {
        channel.writeAndFlush(serializable);
    }

    @Override
    public void onReceived(Serializable serializable) {

    }

    @Override
    public void close() {
        channel.close();
        clientGroup.shutdownGracefully();
        serviceConsumerMediator.shutdownExecutor();
    }

    public void setServiceConsumerMediator(ServiceConsumerMediator serviceConsumerMediator) {
        this.serviceConsumerMediator = serviceConsumerMediator;
    }
}
