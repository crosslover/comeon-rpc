package com.flamingo.comeon.rpc.core;

import java.io.Serializable;

public interface SerializableFactory<O extends Serializable, T> {
    T serialize(O object);
    O deserialize(T target);
}
