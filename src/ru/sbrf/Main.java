package ru.sbrf;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Stringable s = new MyString("HELLO");
        Stringable s1 = new MyString("HELLO");

        System.out.println(s.equals(s1));

        final URL stringPath = Main.class.getClassLoader().getResource(MyString.class.getName().replace('.', '/').concat(".class"));
        final URL basePath = new URL("file:/D:/Andrey/TestApp_Java/TestClassLoaders/out/production/TestClassLoaders/");

        URLClassLoader loader = new URLClassLoader(new URL[]{stringPath, basePath}, Main.class.getClassLoader()){
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                if(name.equals("ru.sbrf.MyString")) {
                    return findClass(name);
                }
                return super.loadClass(name, false);
            }
        };
        Class<MyString> cls = (Class<MyString>)loader.loadClass("ru.sbrf.MyString");
        Stringable o = cls.getConstructor(String.class).newInstance("HELLO");
        System.out.println(s.equals(o));
        foo(o);

        List<URLClassLoader> leakCache = new LinkedList<>();

        for (int i = 0; i < 1_000_000; i++) {
            URLClassLoader l = createClassLoader(basePath);
            l.loadClass("ru.sbrf.MyString");
            leakCache.add(l);
        }
    }

    private static void foo(Stringable s) {
        System.out.println(s.getStr());
    }

    private static URLClassLoader createClassLoader(URL basePath) {
        return new URLClassLoader(new URL[]{basePath}, Main.class.getClassLoader()){
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                if(name.equals("ru.sbrf.MyString")) {
                    return findClass(name);
                }
                return super.loadClass(name, false);
            }
        };
    }
}
