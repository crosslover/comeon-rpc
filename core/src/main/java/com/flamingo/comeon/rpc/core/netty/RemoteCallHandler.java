package com.flamingo.comeon.rpc.core.netty;

import com.flamingo.comeon.rpc.core.Call;
import com.flamingo.comeon.rpc.core.ServiceConsumerMediator;
import com.flamingo.comeon.rpc.core.ServiceProviderMediator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;

import java.io.Serializable;
import java.util.List;

public class RemoteCallHandler extends MessageToMessageCodec<Serializable, Serializable> {

    private static final AttributeKey<Call> callKey = AttributeKey.newInstance("call");

    ServiceProviderMediator serviceProviderMediator;

    ServiceConsumerMediator serviceConsumerMediator;

    public RemoteCallHandler(ServiceProviderMediator serviceProviderMediator, ServiceConsumerMediator serviceConsumerMediator) {
        this.serviceProviderMediator = serviceProviderMediator;
        this.serviceConsumerMediator = serviceConsumerMediator;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, List<Object> out) throws Exception {
        if (msg instanceof Call) {
            ctx.channel().attr(callKey).set((Call)msg);
            out.add(Call.toSerializable((Call) msg));
            return;
        }
        out.add(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Serializable msg, List<Object> out) throws Exception {
        if (msg instanceof Call.ProxyObject) {
            Object ret = serviceProviderMediator.getReturnForCall(Call.fromSerializable(msg));
            ctx.writeAndFlush(ret);
        } else {
            Call call = ctx.channel().attr(callKey).get();
            serviceConsumerMediator.wireUpReturnForCall(call, msg);
        }
    }
}
