package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import xyz.wodamuszyna.koniowonsz.utils.Utils;

public class Invsee implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if(s instanceof Player){
            Inventory inv;
            if(s.hasPermission("nascraft.invsee")){
                if(a.length == 0){
                    s.sendMessage("Uzycie: /invsee nick");
                    return true;
                }else{
                    Player p = Bukkit.getPlayer(a[0]);
                    if(p != null) {
                        if(a.length > 1 && s.hasPermission("nascraft.invsee.armor")){
                            inv = Bukkit.getServer().createInventory(p, 9, "Zbroja");
                            inv.setContents(p.getInventory().getArmorContents());
                            inv.setItem(8, p.getInventory().getItemInOffHand());
                        }else {
                            inv = p.getInventory();
                        }
                        Player sender = Bukkit.getPlayer(s.getName());
                        sender.closeInventory();
                        sender.openInventory(inv);
                    }else{
                        s.sendMessage(Utils.fixColor("&cNie ma takiego gracza"));
                        return true;
                    }
                }
            }else{
                s.sendMessage("Brak uprawnien");
                return true;
            }
        }
        return false;
    }
}
