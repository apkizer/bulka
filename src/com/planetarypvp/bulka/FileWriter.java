package com.planetarypvp.bulka;

import java.io.File;

public interface FileWriter
{
    public void setConfigurableClass(Class clazz);

    public void setConfigurableAnnotation(Class clazz);

    public void setDirectory(String path);

    public void writeClass(Class clazz);

    public File touch(String fileName);
}
