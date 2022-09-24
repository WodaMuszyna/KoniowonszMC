package xyz.wodamuszyna.koniowonsz.bukkit.config;


import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import xyz.wodamuszyna.koniowonsz.utils.ConfigUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SerializableAs("LootboxConfig")
public class LootboxConfig implements ConfigurationSerializable {
    public String broadcastLootbox;
    public String wylosowales;
    public String losowanie;
    public int opoznienie;
    public int waga;
    public List<Los> losy;
    public ItemStack lootbox;
    public String nazwaGui;
    public List<FireworkEffect> efekty;
    public int mocFajerwerki;
    public String broadcastLootboxy;
    public static LootboxConfig deserialize(Map<String, Object> m) { return new LootboxConfig(m); }

    public LootboxConfig(Map<String, Object> m) {
        this.broadcastLootbox = ChatColor.RED + "{NICK} " + ChatColor.GOLD + "otworzyl lootbox!";
        this.wylosowales = ChatColor.GRAY + "Wylosowales {NAZWA}" + ChatColor.GRAY + "!";
        this.losowanie = ChatColor.RED + "Losowanie...";
        this.opoznienie = 20;
        this.nazwaGui = "Lootboxy";
        this.mocFajerwerki = 2;
        ConfigUtils.deserialize(m, this);
    }

    public Map<String, Object> serialize() { return ConfigUtils.serialize(this); }

    @SerializableAs("Los")
    public static class Los
            implements ConfigurationSerializable
    {
        public int prawdopodobienstwo;
        public List<String> komendy;
        public String nagroda;
        public ItemStack ikona;
        public static Los deserialize(Map<String, Object> m) { return new Los(m); }

        public Los(Map<String, Object> m) {
            this.prawdopodobienstwo = 10;
            this.komendy = Arrays.asList(new String[] { "give {NICK} stone 1" });
            this.nagroda = ChatColor.GRAY + "stone";
            ConfigUtils.deserialize(m, this);
        }

        public Map<String, Object> serialize() { return ConfigUtils.serialize(this); }
    }
}