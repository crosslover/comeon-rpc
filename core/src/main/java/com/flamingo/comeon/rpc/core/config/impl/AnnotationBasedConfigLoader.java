package com.flamingo.comeon.rpc.core.config.impl;

import com.flamingo.comeon.rpc.core.config.Config;
import com.flamingo.comeon.rpc.core.config.ConfigLoader;
import com.flamingo.comeon.rpc.core.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AnnotationBasedConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBasedConfigLoader.class);

    private static final String PACKAGE_SEPERATOR = ".";

    private String[] scanningPackages;

    public AnnotationBasedConfigLoader(String[] scanningPackages) {
        this.scanningPackages = scanningPackages;
    }

    @Override
    public Config load() {
        Objects.requireNonNull(scanningPackages);
        Set<Class<?>> classes = findClassesUnderPackages(scanningPackages);

        return new Config();
    }

    private Set<Class<?>> findClassesUnderPackages(String[] scanningPackages) {
        Set<Class<?>> classSet = new HashSet<>(64);
        for (String scanningPackage : scanningPackages) {
            classSet.addAll(findClassesUnderPackage(scanningPackage));
        }
        return classSet;
    }

    private Set<Class<?>> findClassesUnderPackage(String scanningPackage) {
        String scanningPath = scanningPackage.replace(PACKAGE_SEPERATOR, File.separator);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urlEnumeration;
        try {
            urlEnumeration = cl.getResources(scanningPath);
        } catch (IOException e) {
            LOGGER.error("IOException when find classes under package {}:", scanningPath, e);
            throw new RpcException(e);
        }
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            String protocal = url.getProtocol();
            if ("file".equals(protocal)) {
//                findClassesByFile()
            } else if ("jar".equals(protocal)) {

            }
        }
        return new HashSet<>();
    }
}
