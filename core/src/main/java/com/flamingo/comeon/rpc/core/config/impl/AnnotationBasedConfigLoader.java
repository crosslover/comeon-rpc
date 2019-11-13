package com.flamingo.comeon.rpc.core.config.impl;

import com.flamingo.comeon.rpc.core.annotation.Provider;
import com.flamingo.comeon.rpc.core.config.Config;
import com.flamingo.comeon.rpc.core.config.ConfigLoader;
import com.flamingo.comeon.rpc.core.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class AnnotationBasedConfigLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBasedConfigLoader.class);

    private static final String PACKAGE_SEPERATOR = ".";
    private static final String CLASS_SUFFIX = ".class";

    private String[] scanningPackages;

    public AnnotationBasedConfigLoader(String[] scanningPackages) {
        this.scanningPackages = scanningPackages;
    }

    @Override
    public Config load() {
        Objects.requireNonNull(scanningPackages);
        Set<Class<?>> classes = findClassesUnderPackages(scanningPackages);

        Config config = parseConfigFromClasses(classes);
        return config;
    }

    private Config parseConfigFromClasses(Set<Class<?>> classes) {
        Set<Integer> providerSet = new HashSet<>(64);
        for (Class c : classes) {
            if (c.isAnnotationPresent(Provider.class)) {
                Provider provider = (Provider)c.getAnnotation(Provider.class);
                if (providerSet.add(provider.port())) {
                    Config.ProviderConfig providerConfig = new Config.ProviderConfig();
                    providerConfig.setPort(provider.port());
                    List<Class<?>> classList = new ArrayList<>(30);
                    providerConfig.setClasses(classList);
                    classList.add(c);
                } else {

                }
            }
        }
        return null;
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
            throw new IllegalStateException(e);
        }

        Set<Class<?>> classSet = new HashSet<>(32);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            String protocal = url.getProtocol();
            if ("file".equals(protocal)) {
                try {
                    File file = new File(url.toURI());
                    if (file.exists()) {
                        doFindClassesByFile(file, scanningPackage, classSet, cl);
                    }
                } catch (URISyntaxException e) {
                    LOGGER.warn("parse {} error", url);
                }
            }
// TODO
//            else if ("jar".equals(protocal)) {
//            }
        }
        return classSet;
    }

    private void doFindClassesByFile(File file, String packageName, Set<Class<?>> classSet, ClassLoader cl) {
        String fileName = file.getName();
        if (file.isFile() && fileName.endsWith(CLASS_SUFFIX)) {
            String className = packageName + fileName.substring(0, fileName.lastIndexOf(CLASS_SUFFIX));
            try {
                classSet.add(cl.loadClass(className));
            } catch (ClassNotFoundException e) {
                LOGGER.warn("error when load class: {}", className);
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                doFindClassesByFile(subFile, packageName + PACKAGE_SEPERATOR + fileName, classSet, cl);
            }
        }
    }

}
