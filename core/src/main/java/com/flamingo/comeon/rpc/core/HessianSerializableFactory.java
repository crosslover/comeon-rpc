package com.flamingo.comeon.rpc.core;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class HessianSerializableFactory implements SerializableFactory<Serializable, byte[]>{
    @Override
    public byte[] serialize(Serializable serializable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(baos);
        try {
            out.writeObject(serializable);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public Serializable deserialize(byte[] target) {
        ByteArrayInputStream bais = new ByteArrayInputStream(target);
        Hessian2Input in = new Hessian2Input(bais);
        try {
            Object o = in.readObject();
            return (Serializable) o;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
