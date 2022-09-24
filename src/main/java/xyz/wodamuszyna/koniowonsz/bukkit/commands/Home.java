package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;
import xyz.wodamuszyna.koniowonsz.utils.TimeUtil;

public class Home implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s instanceof Player){
            User u = Main.getUserManager().get(s.getName());
            xyz.wodamuszyna.koniowonsz.bukkit.objects.Home h = Main.getHomeManager().getHome(s.getName());
            if(h == null) {
                s.sendMessage("§cNie masz ustawionego domu!");
            }else{
                if(u.lastHomeCommandUsage + 1000 * 10 < System.currentTimeMillis()){
                    u.lastHomeCommandUsage = System.currentTimeMillis();
                    ((Player) s).teleport(h.getLocation());
                    s.sendMessage("§aPrzeteleportowano!");
                }else{
                    s.sendMessage("§cMozesz uzyc tej komendy za: " + TimeUtil.getTimeLeft((u.lastHomeCommandUsage + 1000 * 10 - System.currentTimeMillis())));
                }
            }
            return true;
        }
        return false;
    }
}
