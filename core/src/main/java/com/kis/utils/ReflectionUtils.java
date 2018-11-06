package com.kis.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ReflectionUtils {

    private static Logger logger = Logger.getLogger(ReflectionUtils.class.getName());

    static class PackageInfo {
        private final String name;
        private final Package _package;
        private final boolean annotated;
        private final Set<String> classes;

        PackageInfo(String packageName) {
            name = packageName;
            try {
                Class.forName(packageName + ".package-info");
            } catch (ClassNotFoundException ignored) {

            }
            _package = Package.getPackage(packageName);
            annotated = _package != null && _package.getAnnotations().length > 0;
            classes = new HashSet<>();
        }

        void add(String className) {
            classes.add(className);
        }

        public String getName() {
            return name;
        }

        public Package getPackage() {
            return _package;
        }

        public boolean isAnnotated() {
            return annotated;
        }

        public Stream<String> getClassNames() {
            return classes.stream();
        }

        public Stream<Class> getClasses() {
            ClassLoader classLoader = getClass().getClassLoader();
            return classes.stream().map(name -> {
                try {
                    return Class.forName(name, false, classLoader);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static final String FILE_PREFIX = "file:";
    private static final String JAR_PATH_SEPARATOR = "!/";

    private static final String ROOT_PACKAGE_NAME = "com.kis";

    private static List<PackageInfo> packages;

    private static synchronized List<PackageInfo> getPackages() {
        if (packages == null) {

            Map<String, PackageInfo> packageMap = new HashMap<>();
            scanPackages(ROOT_PACKAGE_NAME, packageMap);
            packages = new ArrayList<>(packageMap.values());

        }
        return packages;
    }

    private static void scanPackages(String rootPackage, Map<String, PackageInfo> packages) {
        try {
            List<String> paths = getResources(rootPackage);
            for (String resourcePath : paths) {
                if (isJarResource(resourcePath))
                    scanJarForPackages(resourcePath, rootPackage, packages);
                else
                    scanDirForPackages(resourcePath, rootPackage, packages);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> getResources(String packageName)
            throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');

        Enumeration resources = classLoader.getResources(path);

        List<String> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            logger.info(resource.getFile());
            dirs.add(URLDecoder.decode(resource.getFile(), "UTF-8"));
        }
        logger.info("Resources = " + dirs.size());
        return dirs;
    }

    private static boolean isJarResource(String resourcePath) {
        return resourcePath.startsWith(FILE_PREFIX) && resourcePath.contains(JAR_PATH_SEPARATOR);
    }

    private static void scanJarForPackages(String resourcePath, String rootPackage, Map<String, PackageInfo> packages)
            throws IOException {
        logger.info("scanJarForPackages");
        String[] split = resourcePath.split(JAR_PATH_SEPARATOR, 2);
        logger.info(resourcePath);
        URL jarUrl = new URL(split[0]);
        logger.info(jarUrl.getFile());
        ZipInputStream zip = new ZipInputStream(jarUrl.openStream());

        ZipEntry zipEntry;
        while ((zipEntry = zip.getNextEntry()) != null) {
            String entryName = zipEntry.getName();
            if (isCollectibleClassPath(entryName)) {
                logger.info(entryName);
                String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                if (!className.startsWith(rootPackage))
                    continue;

                int lastDot = className.lastIndexOf('.');
                String packageName = className.substring(0, lastDot);
                logger.info(packageName);
                packages.computeIfAbsent(packageName, PackageInfo::new).add(className);
            }
        }
    }

    private static void scanDirForPackages(String dirPath, String rootPackage, Map<String, PackageInfo> packages) {
        logger.info("scanDirForPackages");
        File rootDir = new File(dirPath);
        if (!rootDir.exists())
            return;

        Queue<File> queue = new ArrayDeque<>();
        queue.add(rootDir);
        while (!queue.isEmpty()) {
            File dir = queue.remove();
            File[] dirFiles = dir.listFiles();
            if (dirFiles == null)
                continue;

            for (File file : dirFiles) {
                String fileName = file.getName();
                if (file.isDirectory()) {
                    assert !fileName.contains(".");
                    scanDirForPackages(file.getPath(), rootPackage + '.' + fileName, packages);
                } else if (isCollectibleClassPath(file.getPath())) {
                    String className = rootPackage + '.' + fileName.substring(0, fileName.length() - 6);
                    packages.computeIfAbsent(rootPackage, PackageInfo::new).add(className);
                }
            }
        }
    }

    private static boolean isCollectibleClassPath(String path) {
        return path.endsWith(".class") && !path.contains("$");
    }

    public static Map getAnnotatedClasses(Class annotation) {
        logger.info("Searching of annotated classes");
        List<PackageInfo> packages = getPackages();
        logger.info("There are " + packages.size());
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Map map = new HashMap();
        packages.stream()
                .flatMap(packageInfo -> packageInfo.classes.stream())
                .map(name -> loadClass(loader, name))
                .filter(clazz -> clazz.getAnnotation(annotation) != null)
                .forEach(c -> {
                    try {
                        Annotation a = c.getAnnotation(annotation);
                        Class<?> type = a.annotationType();
                        Method m = type.getMethod("type");
                        String key = (String) m.invoke(a);
                        Constructor<?> constructor = c.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        Object value = constructor.newInstance();

                        map.put(key, value);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        return map;
    }

    static private Class loadClass(ClassLoader loader, String name) {
        try {
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
