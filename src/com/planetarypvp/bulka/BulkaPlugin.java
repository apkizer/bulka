package com.planetarypvp.bulka;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BulkaPlugin extends JavaPlugin
{
    private Bulka bulka;

    public void onEnable()
    {
        File settings = new File(getDataFolder(), "com.planetarypvp.bulka.Bulka.txt");
        if(!settings.exists())
        {
           //TODO write file
        }
        bulka = new Bulka(this);
    }

    public void onDisable()
    {

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equals("bulka"))
        {
            if(args.length < 2)
                return false;
            if(args[0].equals("build"))
                return build(args);
        }
        return false;
    }

    public boolean build(String[] args)
    {
        if(args.length == 2)
            bulka.build(args[1]);
        else if(args.length == 3)
            bulka.build(args[1], args[2]);
        else
            return false;
        return true;
    }
}
