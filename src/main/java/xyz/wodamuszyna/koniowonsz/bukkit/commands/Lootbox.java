package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.utils.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.manager.LootboxManager;

import java.util.ArrayList;
import java.util.List;

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
                sendLootboxList(p);
                return true;
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
            if (Main.getLootboxManager().getLootbox(a[0]) == null) {
                s.sendMessage(Utils.fixColor("&cTaki lootbox nie istnieje!"));
                sendLootboxList(p);
                return true;
            }
            for (xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox lb : Main.getLootboxManager().getLootboxy()) {
                if (a[0].equalsIgnoreCase(lb.getName())) {
                    Object[] o = lb.getConfig().losy.toArray();
                    Inventory gui = Main.getInstance().getServer().createInventory(null, (o.length + 8) / 9 * 9, Utils.fixColor(lb.getConfig().nazwaGui));
                    for (int i = 0; i < o.length; i++) {
                        Object l_ = o[i];
                        LootboxConfig.Los los = (LootboxConfig.Los) l_;
                        if (los.ikona != null) {
                            ItemStack is = los.ikona.clone();
                            if (is.getItemMeta() != null && is.getItemMeta().getLore() != null) {
                                ItemMeta im = is.getItemMeta();
                                List<String> lores = im.getLore();
                                ArrayList<String> lores_new = new ArrayList<String>();
                                int nwd = LootboxManager.nwd(los.prawdopodobienstwo, Main.getLootboxManager().getLootbox(lb.getName()).getWagi());
                                for (String st : lores) {
                                    lores_new.add(Utils.fixColor(st.replace("{ILE}", Integer.toString(los.prawdopodobienstwo / nwd))
                                            .replace("{DO_ILU}", Integer.toString(Main.getLootboxManager().getLootbox(lb.getName()).getWagi() / nwd))
                                            .replace("{PROCENT}", Integer.toString(los.prawdopodobienstwo * 100 / Main.getLootboxManager().getLootbox(lb.getName()).getWagi()))));
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
    private void sendLootboxList(Player p){
        p.sendMessage(Utils.fixColor("&7» &cLootboxy&8:"));
        Object asd = Main.getLootboxManager().getLootboxy().toArray()[0];
        xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox lb = (xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox) asd;
        TextComponent c = Component.text(lb.getName().toLowerCase()).clickEvent(ClickEvent.runCommand("/lootbox "+ lb.getName().toLowerCase())).color(NamedTextColor.YELLOW);
        Object[] o = Main.getLootboxManager().getLootboxy().toArray();
        for(int i=1; i < o.length; i++){
            Object lb_o = o[i];
            xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox l = (xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox) lb_o;
            TextComponent comp = Component.text(", "+l.getName().toLowerCase()).clickEvent(ClickEvent.runCommand("/lootbox "+l.getName().toLowerCase())).color(NamedTextColor.YELLOW);
            c.append(comp);
        }
        p.sendMessage(c);
        p.sendMessage(Utils.fixColor("&3Kliknij na lootboxa, aby otworzyc menu dropow!"));
    }
}
