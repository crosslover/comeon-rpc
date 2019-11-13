package com.flamingo.comeon.rpc.core;

import java.io.Serializable;

public interface Transporter {
    void start();

    void send(Serializable serializable);

    void onReceived(Serializable serializable);

    void close();
}
