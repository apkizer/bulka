package com.planetarypvp.bulka.bukkit;

import com.planetarypvp.bulka.YamlFile;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BukkitYamlFile implements YamlFile
{
    private String name;
    private YamlConfiguration yaml;

    public BukkitYamlFile(String name)
    {
        this.name = getName(name);
        yaml = new YamlConfiguration();
        yaml.options().header("Generated by Bulka™. Do not edit 'class' key. \n");
    }

    @Override
    public void set(String key, Object value)
    {
        yaml.set(key, value);
    }

    @Override
    public void save(String path) throws IOException
    {
        yaml.save(path + File.separator + name);
    }

    private String getName(String name)
    {
        return name + "-settings.yml";
    }

}
