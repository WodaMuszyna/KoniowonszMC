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
import xyz.wodamuszyna.koniowonsz.bukkit.config.RewardsConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.BlockListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.InventoryListener;
import xyz.wodamuszyna.koniowonsz.bukkit.listener.ServerListener;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.HomeManager;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.UserManager;
import xyz.wodamuszyna.koniowonsz.database.IConnection;
import xyz.wodamuszyna.koniowonsz.database.MySQLConnection;
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
    public static RewardsConfig rewardsConfig;
    private static IConnection connection;
    public Set<Inventory> inventories = new HashSet<>();
    private static LootboxManager lootboxManager;
    private static UserManager userManager;
    private static HomeManager homeManager;
    public int wagaLootboxow = 0;

    @Override
    public void onEnable(){
        instance = this;
        ConfigurationSerialization.registerClass(LootboxConfig.class);
        ConfigurationSerialization.registerClass(LootboxConfig.Los.class);
        ConfigurationSerialization.registerClass(RewardsConfig.class);
        ConfigurationSerialization.registerClass(RewardsConfig.Reward.class);
        saveResource("config.json", false);
        saveResource("config.yml", false);
        saveResource("rewards.yml", false);
        registerDatabase();
        try {
            getConfig().load(new File(getDataFolder(), "rewards.yml"));
            this.rewardsConfig = (RewardsConfig) getConfig().get("rewards");
            if(this.rewardsConfig == null){
                throw new InvalidConfigurationException("Nie znaleziono elementu w konfiguracji");
            }
        }catch (IOException|InvalidConfigurationException ex){
            getLogger().log(Level.SEVERE, "Nie udało się załadować configu", ex);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        loadLootbox();
        userManager = new UserManager();
        homeManager = new HomeManager();
        Config.init();
        try {
            jda = JDABuilder.createDefault(Config.token).build();
        } catch (LoginException e) {
            e.printStackTrace();
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
        getCommand("sethome").setExecutor(new SetHome());
        getCommand("home").setExecutor(new Home());
        getCommand("rtp").setExecutor(new RTP());
        getCommand("checkuser").setExecutor(new CheckUser());
        getCommand("odbierz").setExecutor(new Odbierz());
    }

    @Override
    public void onDisable() {
        inventories.clear();
        lootboxManager.onDisable();
        ConfigurationSerialization.unregisterClass(LootboxConfig.class);
        ConfigurationSerialization.unregisterClass(LootboxConfig.Los.class);
        ConfigurationSerialization.unregisterClass(RewardsConfig.class);
        ConfigurationSerialization.unregisterClass(RewardsConfig.Reward.class);
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        jda.shutdownNow();
        getConnection().disconnect();
    }

    public void loadLootbox(){
        lootboxManager = new LootboxManager();
        File fl = new File(getDataFolder().getPath() + File.separator + "lootboxy");
        fl.mkdirs();
        for(File f : fl.listFiles()){
            if(f != null){
                String n = f.getName().split("\\.")[0];
                if(n.equalsIgnoreCase("config")) continue;
                if(!Objects.equals(f.getName().split("\\.")[1], "yml")) continue;
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                try {
                    getConfig().load(f);
                    lootboxConfig = (LootboxConfig) getConfig().get("lootboxy");
                    if(lootboxConfig == null){
                        getLogger().warning("Blad configu w pliku: "+f.getName());
                    }
                    new xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox(n, lootboxConfig);
                    getLogger().log(Level.INFO, "Zaladowano lootbox "+n);
                }catch (IOException | InvalidConfigurationException | NullPointerException e){
                    e.printStackTrace();
                    getServer().getPluginManager().disablePlugin(this);
                }
            }
        }

        for(xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox l : Main.getLootboxManager().getLootboxy()){
            this.wagaLootboxow += l.getConfig().waga;
            for(LootboxConfig.Los los : l.getConfig().losy){
                l.setWagi(l.getWagi() + los.prawdopodobienstwo);
            }
        }
    }

    public void registerDatabase(){
        connection = new MySQLConnection(
                getConfig().getString("mysql.host"),
                getConfig().getInt("mysql.port"),
                getConfig().getString("mysql.username"),
                getConfig().getString("mysql.password"),
                getConfig().getString("mysql.database"));
        boolean connected = connection.connect();
        if(connected) {
            connection.update(true, "CREATE TABLE IF NOT EXISTS `koniowonsz_users` (`id` INT(2) NOT NULL PRIMARY KEY AUTO_INCREMENT, `username` VARCHAR(16) NOT NULL, `ip` VARCHAR(16) NOT NULL, `lastOnline` TIMESTAMP NULL, `lastWorld` VARCHAR(20) NOT NULL, `lastX` DOUBLE NOT NULL, `lastY` DOUBLE NOT NULL, `lastZ` DOUBLE NOT NULL, `lastDailyRewardDay` INT(2) DEFAULT 0, `lastDailyRewardClaim` BIGINT(100) NULL);");
            connection.update(true, "CREATE TABLE IF NOT EXISTS `koniowonsz_homes` (`owner` VARCHAR(100) NOT NULL PRIMARY KEY COLLATE 'utf8_polish_ci', `world` VARCHAR(255) NOT NULL COLLATE 'utf8_polish_ci', `x` DOUBLE NOT NULL, `y` DOUBLE NOT NULL, `z` DOUBLE NOT NULL, `yaw` FLOAT NOT NULL, `pitch` FLOAT NOT NULL);");

        }

    }

    public void reloadLootbox(){
        getLootboxManager().getLootboxy().clear();
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
    public static UserManager getUserManager(){ return userManager; }
    public static HomeManager getHomeManager(){ return homeManager; }
    public static IConnection getConnection(){ return connection; }
}
