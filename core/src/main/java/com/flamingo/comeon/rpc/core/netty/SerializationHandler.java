package com.flamingo.comeon.rpc.core.netty;

import com.flamingo.comeon.rpc.core.SerializableFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class SerializationHandler extends ByteToMessageCodec<Serializable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializableFactory.class);
    SerializableFactory<Serializable, byte[]> serializableFactory;

    public SerializationHandler(SerializableFactory<Serializable, byte[]> serializableFactory) {
        this.serializableFactory = serializableFactory;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Serializable o, ByteBuf byteBuf) throws Exception {
        byte[] bytes = serializableFactory.serialize(o);
        System.out.println("===============");
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        for (int i = 0; i < bytes.length; i ++) {
            byteArrayList.add(bytes[i]);
        }
        byteArrayList.stream().forEach(System.out::println);
        System.out.println("===============");
        ArrayList<Character> charArrayList = new ArrayList<>();
        for (int i = 0; i < bytes.length; i ++) {
            charArrayList.add((char) bytes[i]);
        }
        charArrayList.stream().forEach(System.out::print);
        System.out.println();
        byteBuf.writeBytes(bytes);
        LOGGER.debug(byteBuf.toString());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        System.out.println("===============");
        byteBuf.readBytes(bytes);
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        for (int i = 0; i < bytes.length; i ++) {
            byteArrayList.add(bytes[i]);
        }
        byteArrayList.stream().forEach(System.out::println);
        System.out.println("===============");
        ArrayList<Character> charArrayList = new ArrayList<>();
        for (int i = 0; i < bytes.length; i ++) {
            charArrayList.add((char) bytes[i]);
        }
        charArrayList.stream().forEach(System.out::print);
        System.out.println();
        list.add(serializableFactory.deserialize(bytes));
        LOGGER.debug(list.toString());
    }
}
