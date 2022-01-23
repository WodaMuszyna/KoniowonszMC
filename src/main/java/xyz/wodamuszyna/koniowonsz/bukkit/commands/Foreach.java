package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Foreach implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (s.hasPermission("nascraft.foreach")) {
            if (a.length < 2) {
                s.sendMessage("Przykladowe uzycie: /foreach -* /minecraft:kick {player} Nju edyszyn");
                return true;
            }
            String msg = a[1];
            for (int i = 2; i < a.length; ++i) {
                msg = String.valueOf(msg) + " " + a[i];
            }
            final boolean testPermission = a[0].startsWith("-");
            String perm = a[0];
            if (testPermission) {
                perm = perm.substring(1);
            }
            if (msg.startsWith("/")) {
                msg = msg.substring(1);
                if (s instanceof Player) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission(perm) != testPermission) {
                            Bukkit.getServer().dispatchCommand(s, msg.replace("{player}", p.getName()));
                        }
                    }
                }
            } else if (s instanceof Player) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission(perm) != testPermission) {
                        ((Player) s).chat(msg.replace("{player}", p.getName()));
                    }
                }
            }
        }else{
            s.sendMessage("Brak uprawnien");
        }
        return false;
    }
}
