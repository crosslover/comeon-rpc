package com.flamingo.comeon.rpc.core.service.impl;

import com.flamingo.comeon.rpc.core.config.ConfigLoader;
import com.flamingo.comeon.rpc.core.service.Manager;

public class GenericManager implements Manager {

    private ConfigLoader configLoader;

    @Override
    public void init() {
        findConfigLoader();
    }



    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    private void findConfigLoader() {

    }
}
