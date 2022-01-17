package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;

public class Lootbox implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s instanceof Player){
            Player p = Bukkit.getPlayer(s.getName());
            if(p != null) p.openInventory(Main.getLootboxManager().gui);
        }
        return false;
    }
}
