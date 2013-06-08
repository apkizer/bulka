package com.planetarypvp.bulka;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: alexkizer
 * Date: 6/6/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BukkitBulkaPackage implements BulkaPackage
{
    @Override
    public void read(JarFile jarFile)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void processInput(InputStream input) throws IOException
    {

    }

    @Override
    public boolean exists()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getOut()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeader()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
