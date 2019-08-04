package com.flamingo.comeon.rpc.core.main;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws Exception {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls =  cl.getResources("com/flamingo/comeon/rpc/core/config");
        while (urls.hasMoreElements()) {
            System.out.println(urls.nextElement());
        }

        URL[] urls1 = ((URLClassLoader)cl).getURLs();
        for (URL url : urls1) {
            System.out.println(url);
        }
    }
}
