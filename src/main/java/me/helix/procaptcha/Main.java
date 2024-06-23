package me.helix.procaptcha;

import me.helix.procaptcha.Data.DataFile;
import me.helix.procaptcha.Listners.Listners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    public static Main instance;
    public HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>();
    public DataFile data = new DataFile(this);

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void onEnable() {
        instance = this;

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }

        data.saveDaultthis();

        Bukkit.getConsoleSender().sendMessage(color("&7============================================="));
        Bukkit.getConsoleSender().sendMessage(color("&eProCaptcha &7: &a1&7.&a1"));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(color("&eMade by &7: &eChelsea1124&7/&eHELIX"));
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(color("&eVersion &7: &a1&7.&a8&e+"));
        Bukkit.getConsoleSender().sendMessage(color("&7============================================="));

        this.getServer().getPluginManager().registerEvents(new Listners(this), instance);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
