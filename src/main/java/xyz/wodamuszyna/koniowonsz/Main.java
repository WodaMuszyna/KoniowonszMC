package xyz.wodamuszyna.koniowonsz;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wodamuszyna.koniowonsz.bukkit.commands.DajLootboxy;
import xyz.wodamuszyna.koniowonsz.bukkit.commands.Lootbox;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.BlockListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.InventoryListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.ServerListener;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;
import xyz.wodamuszyna.koniowonsz.discord.commands.Reply;
import xyz.wodamuszyna.koniowonsz.discord.commands.Run;
import xyz.wodamuszyna.koniowonsz.discord.commands.Say;
import xyz.wodamuszyna.koniowonsz.discord.commands.Send;
import xyz.wodamuszyna.koniowonsz.bukkit.commands.Koniowonsz;
import xyz.wodamuszyna.koniowonsz.discord.Config;
import xyz.wodamuszyna.koniowonsz.discord.GuildListener;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    public static Main instance;
    public static JDA jda = null;
    private boolean maintenance = false;
    public LootboxConfig lootboxConfig;
    private static LootboxManager lootboxManager;
    public int sumaWag = 0;

    public void onEnable(){
        instance = this;
        ConfigurationSerialization.registerClass(LootboxConfig.class);
        ConfigurationSerialization.registerClass(LootboxConfig.Los.class);
        saveResource("config.json", false);
        saveResource("config.yml", false);
        try {
            getConfig().load(new File(getDataFolder(), "config.yml"));
            this.lootboxConfig = (LootboxConfig) getConfig().get("lootboxy");
            if(this.lootboxConfig == null){
                throw new InvalidConfigurationException("Nie znaleziono elementu w konfiguracji");
            }
        }catch (IOException| InvalidConfigurationException ex){
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.sumaWag = 0;
        for(LootboxConfig.Los l : this.lootboxConfig.losy){
            this.sumaWag += l.prawdopodobienstwo;
        }
        lootboxManager = new LootboxManager();
        lootboxManager.load();
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
    }

    @Override
    public void onDisable() {
        lootboxManager.onDisable();
        ConfigurationSerialization.unregisterClass(LootboxConfig.class);
        ConfigurationSerialization.unregisterClass(LootboxConfig.Los.class);
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        jda.shutdownNow();
    }

    public static Main getInstance(){ return instance; }
    public static JDA getJDA(){ return jda; }
    public boolean isMaintenance(){ return maintenance; }
    public void toggleMaintenance(){ maintenance = !maintenance; }
    public static LootboxManager getLootboxManager(){ return lootboxManager; }
}
