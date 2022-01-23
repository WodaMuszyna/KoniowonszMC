package xyz.wodamuszyna.koniowonsz.bukkit.config;


import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

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
        this.broadcastLootbox = ChatColor.RED + "{NICK} " + ChatColor.GOLD + "otworzyl puszke pandory!";
        this.wylosowales = ChatColor.GRAY + "Wylosowales {NAZWA}" + ChatColor.GRAY + "!";
        this.losowanie = ChatColor.RED + "Losowanie...";
        this.opoznienie = 20;
        this.nazwaGui = "Pandory";
        this.mocFajerwerki = 2;
        deserialize(m, this);
    }

    public static void deserialize(Map<String, Object> m, Object o) {
        for (int i=0; i < o.getClass().getFields().length; i++) {
            Field f = o.getClass().getFields()[i];
            if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && m.containsKey(f.getName())) {
                try {
                    f.set(o, m.get(f.getName()));
                }
                catch (IllegalArgumentException|IllegalAccessException|NullPointerException ex2) {
                    if (!(ex2 instanceof NullPointerException)) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
    }

    public static Map<String, Object> serialize(Object o) {
        TreeMap<String, Object> m = new TreeMap<String, Object>();
        for (int i = 0; i < o.getClass().getFields().length; i++) {
            Field f = o.getClass().getFields()[i];
            if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
                try {
                    if (f.getType() == String.class) {
                        m.put(f.getName(), f.get(o));
                    } else {

                        m.put(f.getName(), f.get(o));
                    }

                } catch (IllegalArgumentException|IllegalAccessException|NullPointerException ex2) {
                    if (!(ex2 instanceof NullPointerException)) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
        return m;
    }


    public Map<String, Object> serialize() { return serialize(this); }

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
            this.komendy = Arrays.asList(new String[] { "burn {NICK} 20s" });
            this.nagroda = ChatColor.LIGHT_PURPLE + "diamenty";
            LootboxConfig.deserialize(m, this);
        }

        public Map<String, Object> serialize() { return LootboxConfig.serialize(this); }
    }
}