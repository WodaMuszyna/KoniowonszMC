package xyz.wodamuszyna.koniowonsz.bukkit.manager;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;

import java.util.*;

public class LootboxManager {
    public final Random R = new Random();
    private static final String[] JEZYK = { "lootboxa", "lootboxy", "lootboxow", "lootboxa", "lootboxy", "lootboxow" };
    public LootboxManager config;
    public Inventory gui;

    public String odmien(int ilosc, int offset) {
        if (ilosc == 0) {
            return JEZYK[offset + 2];
        }
        if (ilosc == 1) {
            return JEZYK[offset];
        }
        if (ilosc > 1 && ilosc < 5) {
            return JEZYK[offset + 1];
        }
        if (ilosc < 22) {
            return JEZYK[offset + 2];
        }
        ilosc %= 10;
        if (ilosc > 1 && ilosc < 5) {
            return JEZYK[offset + 1];
        }
        return JEZYK[offset + 2];
    }

    public Player searchPlayer(String username) {
        Player p = Main.getInstance().getServer().getPlayer(username);
        if (p != null) {
            return p;
        }
        p = Main.getInstance().getServer().getPlayerExact(username);
        if (p != null) {
            return p;
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (p2.getName().equalsIgnoreCase(username)) {
                return p2;
            }
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (p2.getName().contains(username)) {
                if (p != null) {
                    return null;
                }
                p = p2;
            }
        }
        if (p != null) {
            return p;
        }
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (p2.getName().toLowerCase().contains(username.toLowerCase())) {
                if (p != null) {
                    return null;
                }
                p = p2;
            }
        }
        return p;
    }

    public void onDisable(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() == gui){
                p.closeInventory();
            }
        }
    }

    public void load(){
        Object[] o = Main.getInstance().lootboxConfig.losy.toArray();
        gui = Main.getInstance().getServer().createInventory(null, (o.length + 8) / 9 * 9, Utils.fixColor(Main.getInstance().lootboxConfig.nazwaGui));
        for(int i=0; i < o.length; i++){
            Object l = o[i];
            LootboxConfig.Los los = (LootboxConfig.Los) l;
            if(los.ikona == null){
                return;
            }
            ItemStack is = los.ikona.clone();
            if (is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                ItemMeta im = is.getItemMeta();
                List<String> lores = im.getLore();
                ArrayList<String> lores_new = new ArrayList<String>();
                int nwd = nwd(los.prawdopodobienstwo, Main.getInstance().sumaWag);
                for (String s : lores) {
                    lores_new.add(Utils.fixColor(s.replace("{ILE}", Integer.toString(los.prawdopodobienstwo / nwd))
                            .replace("{DO_ILU}", Integer.toString(Main.getInstance().sumaWag / nwd))
                            .replace("{PROCENT}", Integer.toString(los.prawdopodobienstwo * 100 / Main.getInstance().sumaWag))));
                }
                im.setDisplayName(Utils.fixColor(im.getDisplayName()));
                im.setLore(lores_new);
                is.setItemMeta(im);
            }
            gui.setItem(i, is);
        }
    }

    public static int nwd(int a, int b) {
        while (b != 0) {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    public void give(Player p, int ile) {
        ItemStack pandora = Main.getInstance().lootboxConfig.lootbox.clone();
        pandora.setAmount(ile);
        Collection<ItemStack> itemki = p.getInventory().addItem(new ItemStack[] { pandora }).values();
        for (ItemStack i : itemki) {
            p.getWorld().dropItemNaturally(p.getLocation(), i);
        }
    }
}