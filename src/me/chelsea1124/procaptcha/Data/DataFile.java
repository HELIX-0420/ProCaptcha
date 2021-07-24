package me.chelsea1124.procaptcha.Data;

import me.chelsea1124.procaptcha.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataFile {

    private Main main;
    public DataFile  (Main main) {
        this.main = main;
        saveDaultthis();
    }

    private FileConfiguration configuration = null;
    private File configFile = null;

    public void reloadConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.main.getDataFolder(), "player-joins.yml");
        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.main.getResource("player-joins.yml");
        if(defaultStream != null){
            YamlConfiguration dafualtconfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configuration.setDefaults(dafualtconfig);
        }
    }
    public FileConfiguration getConfig(){
        if(this.configuration == null)
            reloadConfig();
        return this.configuration;
    }
    public void SavethisConfig(){
        if(this.configuration == null || this.configFile == null)
            return;
        try {
            this.getConfig().save(this.configFile);
        }catch (Exception e){
            main.getLogger().log(Level.SEVERE, "Could not save this File");
        }
    }
    public void saveDaultthis() {
        if (this.configuration == null)
            this.configFile = new File(this.main.getDataFolder(), "player-joins.yml");
        if(!this.configFile.exists())
            this.main.saveResource("player-joins.yml", false);

    }

}
