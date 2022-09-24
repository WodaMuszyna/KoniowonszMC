package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.Home;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;

public class SetHome implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s instanceof Player) {
            Home h = Main.getHomeManager().getHome(s.getName());
            Player p = Bukkit.getPlayer(s.getName());
            if(p == null) s.sendMessage("co to sie stanelo");
            if(h == null) {
                new Home(s.getName(), p.getLocation());
                s.sendMessage("§aUstawiono dom!");
            } else {
                h.setWorld(((Player) s).getLocation().getWorld().getName());
                h.setX(((Player) s).getLocation().getX());
                h.setY(((Player) s).getLocation().getY());
                h.setZ(((Player) s).getLocation().getZ());
                h.setYaw(((Player) s).getLocation().getYaw());
                h.setPitch(((Player) s).getLocation().getPitch());
                h.update();
                s.sendMessage("§aZmieniono dom!");
            }
        }
        return false;
    }
}
