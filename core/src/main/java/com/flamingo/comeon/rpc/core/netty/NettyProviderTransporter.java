package com.flamingo.comeon.rpc.core.netty;

import com.flamingo.comeon.rpc.core.HessianSerializableFactory;
import com.flamingo.comeon.rpc.core.IPPortServiceConsumerConfig;
import com.flamingo.comeon.rpc.core.IPPortServiceProviderConfig;
import com.flamingo.comeon.rpc.core.ServiceConsumerConfig;
import com.flamingo.comeon.rpc.core.ServiceProviderConfig;
import com.flamingo.comeon.rpc.core.ServiceProviderMediator;
import com.flamingo.comeon.rpc.core.Transporter;
import com.flamingo.comeon.rpc.core.common.Assert;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

import java.io.Serializable;

public class NettyProviderTransporter implements Transporter {

    ServiceProviderConfig serviceProviderConfig;
    ServiceProviderMediator serviceProviderMediator;
    NioEventLoopGroup bossGroup;
    NioEventLoopGroup workerGroup;
    ChannelFuture serverChannelFuture;

    public NettyProviderTransporter(ServiceProviderConfig serviceProviderConfig) {
        Assert.isTrue(serviceProviderConfig instanceof IPPortServiceProviderConfig);
        this.serviceProviderConfig = serviceProviderConfig;
    }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverChannelFuture = new ServerBootstrap()
                            .group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler())
                            .childHandler(new ChannelInitializer<Channel>() {
                                @Override
                                protected void initChannel(Channel channel) throws Exception {
                                    channel.pipeline()
                                            .addLast(new LoggingHandler())
//                                            .addLast(new LineBasedFrameDecoder(1024))
                                            .addLast(new SerializationHandler(new HessianSerializableFactory()))
                                            .addLast(new RemoteCallHandler(serviceProviderMediator, null));
                                }
                            })
                            .bind(((IPPortServiceProviderConfig) serviceProviderConfig).getPort())
                            .sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                close();
            }
        }));
    }

    @Override
    public void send(Serializable serializable) {
    }

    @Override
    public void onReceived(Serializable serializable) {

    }

    @Override
    public void close() {
        try {
            serverChannelFuture.channel().closeFuture().sync();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setServiceProviderMediator(ServiceProviderMediator serviceProviderMediator) {
        this.serviceProviderMediator = serviceProviderMediator;
    }
}
