package com.planetarypvp.bulka;

import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: alexkizer
 * Date: 6/6/13
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BulkaPackage
{
    public void read(JarFile jarFile);

    public boolean exists();

    public String getOut();

    public String getHeader();
}
