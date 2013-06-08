package com.planetarypvp.bulka;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class Bulka
{
    private BulkaPlugin plugin;
    private Logger logger;
    private FileWriter writer;

    private String in;
    private String inDir;
    private String out;
    private JarFile jar;
    private Class configurable = null;
    private Class configurableAnnotation = null;

    private URLClassLoader classLoader;

    public Bulka(BulkaPlugin plugin)
    {
        this.plugin = plugin;
        logger = plugin.getLogger();
        this.writer = new BukkitYamlFileWriter(this);
    }

    public BulkaPlugin getPlugin()
    {
        return plugin;
    }

    public void build(String in, String out)  //TODO handle out, if out is null, etc.
    {
        if(out == null)
        {
            build(in);
            return;
        }

        logger.info("Building settings from " + in + " to " + out);
        inDir = plugin.getDataFolder().getAbsoluteFile().getParent();
        this.in = inDir + File.separator + in;//TODO decompose
        this.out = out;
        buildSettings(this.in);
    }

    public void build(String in)
    {
        out = createNewSettings(in).getAbsolutePath();
        build(in, out);
    }

    private File createNewSettings(String in)
    {
        File def = new File(plugin.getServer().getPluginManager().getPlugin(in.substring(0, in.length() - 4)).getDataFolder(), "settings");
        if(!def.exists())
            def.mkdir();
        String settingsName = in.substring(0, in.length() - 4).toLowerCase() + "-settings";
        File o = new File(def, settingsName);
        o.mkdir();
        return o;
    }


    public Class<?> getConfigurableAnnotation(ArrayList<Class<?>> classes)
    {
        if(configurableAnnotation == null)
            findConfigurableAnnotation(classes);
        return configurableAnnotation;
    }

    private void findConfigurableAnnotation(ArrayList<Class<?>> classes)
    {
        try {
            for(Class c : classes)
            {
                if(c.getSimpleName().equals("Configurable")) //TODO
                {
                     configurableAnnotation = c;
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding configurable annotation.");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Found configurable annotation: " + configurableAnnotation.getName());
    }

    /*private void findPackage(JarFile jarFile)
    {
        File bulkaPackage;

        while(jarFile.entries().hasMoreElements())
        {
            if(jarFile.entries().nextElement().getName().contains("bulka.package.yml"))
            {
                bulkaPackage = jarFile.entries().nextElement().
            }
        }
    } */

    private void buildSettings(String jarFile)
    {
        ArrayList<Class<?>> classes;


        try {
            classes = getClasses(jarFile);
        } catch (IOException e) {
            logger.severe("Could not retrieve classes from specified .jar file.");
            e.printStackTrace();
            return;
        }

        writer.setConfigurableAnnotation(getConfigurableAnnotation(classes));
        ArrayList<Class<?>> annotatedClasses;

        try {
            annotatedClasses = getConfigurableClasses(classes);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return;
        }

        writeFiles(annotatedClasses);
    }

    private ArrayList<Class<?>> getClasses(String jarFile) throws IOException
    {
        jar = new JarFile(jarFile);

        Enumeration<JarEntry> entries = jar.entries();
        URL[] urls = { new URL("file:" + in) };
        classLoader = new URLClassLoader(urls);
        ArrayList<Class<?>> classes = new ArrayList<>();

        while(entries.hasMoreElements())
        {
            JarEntry entry = entries.nextElement();

            if(entry.isDirectory() || !entry.getName().endsWith(".class"))
                continue;

            String className = entry.getName().substring(0, entry.getName().length() - 6);

            try {
                classes.add(classLoader.loadClass(className.replace('/', '.')));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }

    private ArrayList<Class<?>> getConfigurableClasses(ArrayList<Class<?>> classes) throws Exception
    {
        ArrayList<Class<?>> annotated = new ArrayList<>();

        for(Class c : classes)//TODO decomp this part into findConfigurableClass();
        {
            if(c.getSimpleName().equals("ConfigurableClass"))//TODO get this string from settings
                configurable = c;
        }

        if(configurable == null)
        {
            throw new Exception("Could not locate Configurable class.");
        }

        for(Class c : classes)
        {
            if(configurable.isAssignableFrom(c) && !c.equals(configurable))
                annotated.add(c);
        }

        System.out.println("annotated: " + annotated.size());
        return annotated;
    }

    private static void registerSerializableClasses(ArrayList<Class<? extends ConfigurationSerializable>> serializable)
    {
        for(Class<? extends ConfigurationSerializable> clazz : serializable)
        {
            ConfigurationSerialization.registerClass(clazz);
        }
    }

    private void writeFiles(ArrayList<Class<?>> annotatedClasses)
    {
        writer.setConfigurableClass(configurable);
        writer.setDirectory(out);
        ArrayList<Exception> errors = new ArrayList<>();

        for(Class c : annotatedClasses)
        {
            try {
                writer.writeClass(c);
            } catch (Exception e) {
                errors.add(e);
                e.printStackTrace();
            }
        }

        writer.touch("README.md");

        if(errors.size() > 0)
        {
            logger.info("Settings built, but there were errors.");
            for(Exception e : errors)
            {
                logger.warning("- " + e.getMessage());
            }
            return;
        }

        logger.info("Settings built successfully!");
        return;
    }
}
