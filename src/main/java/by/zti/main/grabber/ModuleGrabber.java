package by.zti.main.grabber;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This singleton is designed to look for jar files is specific directory and load them dynamically at runtime. It uses
 * marker annotation "Hookable" with it's "value" field containing init method name to find appropriate start class in jar
 * file to initialize module. Init method must be public and must have no parameter.
 *
 * @author Yan Frankovski
 * @since ZTIU 1.0.6
 */
public class ModuleGrabber {
    /** Singleton instance field */
    private static ModuleGrabber instance;
    /** list of classes for searching the init point */
    private ArrayList<Class> classes;
    /** List of jars in module directory */
    private ArrayList<File> jars;
    /** List of class names for class loading */
    private ArrayList<String> classNames;

    /**
     * Simple initializing constructor
     */
    private ModuleGrabber() {
        classes = new ArrayList<>();
        jars = new ArrayList<>();
        classNames = new ArrayList<>();
    }

    /**
     * This method will scan given directory for jar files and load them to the JVM memory.
     * @param dir - search directory path.
     * @return - instance of ModuleGrabber for inline usage.
     */
    public ModuleGrabber grab(String dir){
        classes.clear();
        jars.clear();
        jars.addAll(Arrays.asList(new File(dir).listFiles((dir1, name) -> name.endsWith("jar"))));
        parseClassNames();
        loadClasses();
        return this;
    }

    /**
     * Invoke this method after grabbing to initialize given init points in modules (if any)
     */
    public void initModules() {
        for (Class clazz: classes){
            Hookable hookable = (Hookable) clazz.getAnnotation(Hookable.class);
            new Thread(() -> {
                if(hookable!=null){
                    try {
                        clazz.getMethod(hookable.value()).invoke(clazz.getConstructor().newInstance());
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void loadClasses() {
        URL[] urls = new URL[jars.size()];
        for (int i = 0; i < urls.length; i++) {
            try {
                urls[i] = jars.get(i).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        URLClassLoader loader = URLClassLoader.newInstance(urls);
        classNames.forEach(s -> {
            try {
                classes.add(loader.loadClass(s));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void parseClassNames() {
        jars.forEach(file -> {
            try {
                Enumeration<JarEntry> enumeration = new JarFile(file).entries();
                while (enumeration.hasMoreElements()){
                    JarEntry entry = enumeration.nextElement();
                    if (entry.isDirectory() || !entry.getName().endsWith(".class")){continue;}
                    classNames.add(entry.getName().substring(0, entry.getName().length()-6).replace('/', '.'));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /** Singleton method. Use it to obtain new instance.
     * @return - ModuleGrabber instance.
     */
    public static ModuleGrabber getInstance(){
        if(instance == null){
            instance = new ModuleGrabber();
        }
        return instance;
    }
}
