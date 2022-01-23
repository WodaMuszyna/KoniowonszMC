package xyz.wodamuszyna.koniowonsz;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wodamuszyna.koniowonsz.bukkit.commands.*;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.BlockListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.InventoryListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.ServerListener;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;
import xyz.wodamuszyna.koniowonsz.discord.commands.Reply;
import xyz.wodamuszyna.koniowonsz.discord.commands.Run;
import xyz.wodamuszyna.koniowonsz.discord.commands.Say;
import xyz.wodamuszyna.koniowonsz.discord.commands.Send;
import xyz.wodamuszyna.koniowonsz.discord.Config;
import xyz.wodamuszyna.koniowonsz.discord.GuildListener;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static Main instance;
    public static JDA jda = null;
    private boolean maintenance = false;
    public LootboxConfig lootboxConfig;
    public Map<String, LootboxConfig> lootboxy = new HashMap<>();
    public Map<LootboxConfig, Integer> wagi = new HashMap<>();
    public Set<Inventory> inventories = new HashSet<>();
    private static LootboxManager lootboxManager;
    public int wagaLootboxow = 0;

    @Override
    public void onEnable(){
        instance = this;
        ConfigurationSerialization.registerClass(LootboxConfig.class);
        ConfigurationSerialization.registerClass(LootboxConfig.Los.class);
        saveResource("config.json", false);
        loadLootbox();
        Config.init();
        try {
            jda = JDABuilder.createDefault(Config.token).build();
        }catch (LoginException ex){
            ex.printStackTrace();
        }
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(Config.ownerId);
        builder.setPrefix("$");
        builder.setActivity(Activity.watching("RossoTV"));
        builder.addCommands(new Send(), new Say(), new Reply(), new Run());
        jda.addEventListener(new GuildListener());
        jda.addEventListener(builder.build());
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getCommand("koniowonsz").setExecutor(new Koniowonsz());
        getCommand("lootbox").setExecutor(new Lootbox());
        getCommand("dajlootboxy").setExecutor(new DajLootboxy());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("foreach").setExecutor(new Foreach());
    }

    @Override
    public void onDisable() {
        inventories.clear();
        lootboxManager.onDisable();
        ConfigurationSerialization.unregisterClass(LootboxConfig.class);
        ConfigurationSerialization.unregisterClass(LootboxConfig.Los.class);
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        jda.shutdownNow();
    }

    public void loadLootbox(){
        for(File f : getDataFolder().listFiles()){
            if(f != null){
                String n = f.getName().split("\\.")[0];
                if(!Objects.equals(f.getName().split("\\.")[1], "yml")) continue;
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                try {
                    getConfig().load(f);
                    lootboxConfig = (LootboxConfig) getConfig().get("lootboxy");
                    if(lootboxConfig == null){
                        getLogger().warning("Blad configu w pliku: "+f.getName());
                    }
                    lootboxy.put(n, lootboxConfig);
                    getLogger().log(Level.INFO, "Zaladowano lootbox "+n);
                }catch (IOException | InvalidConfigurationException | NullPointerException e){
                    e.printStackTrace();
                    getServer().getPluginManager().disablePlugin(this);
                }
            }
        }
        for(Map.Entry<String, LootboxConfig> m : Main.getInstance().lootboxy.entrySet()) {
            this.wagaLootboxow += m.getValue().waga;
            for (LootboxConfig.Los los : m.getValue().losy) {
                Main.getInstance().wagi.put(m.getValue(), Main.getInstance().wagi.containsKey(m.getValue()) ? Main.getInstance().wagi.get(m.getValue()) + los.prawdopodobienstwo : 0);
            }
        }
        lootboxManager = new LootboxManager();
    }

    public void reloadLootbox(){
        this.lootboxy.clear();
        this.wagi.clear();
        this.wagaLootboxow = 0;
        this.lootboxConfig = null;
        lootboxManager = null;
        loadLootbox();
    }

    public static Main getInstance(){ return instance; }
    public static JDA getJDA(){ return jda; }
    public boolean isMaintenance(){ return maintenance; }
    public void toggleMaintenance(){ maintenance = !maintenance; }
    public static LootboxManager getLootboxManager(){ return lootboxManager; }
}
