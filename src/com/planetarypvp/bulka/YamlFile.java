package com.planetarypvp.bulka;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: alexkizer
 * Date: 6/5/13
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface YamlFile
{
    public void set(String key, Object value);

    public void save(String path) throws IOException;
}
