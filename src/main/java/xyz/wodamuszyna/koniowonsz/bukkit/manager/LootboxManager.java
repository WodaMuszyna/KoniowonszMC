package xyz.wodamuszyna.koniowonsz.bukkit.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.wodamuszyna.koniowonsz.Main;

import java.util.*;

public class LootboxManager {
    public final Random R = new Random();
    private static final String[] JEZYK = { "lootboxa", "lootboxy", "lootboxow", "lootboxa", "lootboxy", "lootboxow" };
    public LootboxManager config;

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
            if(p.getOpenInventory() != null){
                p.closeInventory();
            }
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

    public void give(Player p, String typ, int ile) {
        ItemStack lootbox = Main.getInstance().lootboxy.get(typ).lootbox.clone();
        lootbox.setAmount(ile);
        Collection<ItemStack> itemki = p.getInventory().addItem(new ItemStack[] { lootbox }).values();
        for (ItemStack i : itemki) {
            p.getWorld().dropItemNaturally(p.getLocation(), i);
        }
    }
}