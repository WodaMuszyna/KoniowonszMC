package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lootbox implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if (s instanceof Player) {
            Player p = Bukkit.getPlayer(s.getName());
            if (p == null) return true;
            if (a.length == 0) {
                s.sendMessage("Uzycie: /lootbox <typ>");
                return true;
            }
            if(a[0].equalsIgnoreCase("list")){
            }
            if(a[0].equalsIgnoreCase("reload")){
                if(p.isOp()) {
                    Main.getInstance().reloadLootbox();
                    s.sendMessage(Utils.fixColor("&aKonfiguracja zostala przeladowana"));
                }else{
                    s.sendMessage("Brak uprawnien");
                }
                return true;
            }
            if (Main.getInstance().lootboxy.get(a[0]) == null) {
                s.sendMessage(Utils.fixColor("&cTaki lootbox nie istnieje!"));
                return true;
            }
            for (Map.Entry<String, LootboxConfig> m : Main.getInstance().lootboxy.entrySet()) {
                if (a[0].equalsIgnoreCase(m.getKey())) {
                    Object[] o = m.getValue().losy.toArray();
                    Inventory gui = Main.getInstance().getServer().createInventory(null, (o.length + 8) / 9 * 9, Utils.fixColor(m.getValue().nazwaGui));
                    for (int i = 0; i < o.length; i++) {
                        Object l_ = o[i];
                        LootboxConfig.Los los = (LootboxConfig.Los) l_;
                        if (los.ikona != null) {
                            ItemStack is = los.ikona.clone();
                            if (is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                                ItemMeta im = is.getItemMeta();
                                List<String> lores = im.getLore();
                                ArrayList<String> lores_new = new ArrayList<String>();
                                int nwd = LootboxManager.nwd(los.prawdopodobienstwo, Main.getInstance().wagi.get(m.getValue()));
                                for (String st : lores) {
                                    lores_new.add(Utils.fixColor(st.replace("{ILE}", Integer.toString(los.prawdopodobienstwo / nwd))
                                            .replace("{DO_ILU}", Integer.toString(Main.getInstance().wagi.get(m.getValue()) / nwd))
                                            .replace("{PROCENT}", Integer.toString(los.prawdopodobienstwo * 100 / Main.getInstance().wagi.get(m.getValue())))));
                                }
                                im.setDisplayName(Utils.fixColor(im.getDisplayName()));
                                im.setLore(lores_new);
                                is.setItemMeta(im);
                            }
                            gui.setItem(i, is);
                        }
                    }
                    p.openInventory(gui);
                    Main.getInstance().inventories.add(gui);
                }
            }
        }
        return false;
    }
}
