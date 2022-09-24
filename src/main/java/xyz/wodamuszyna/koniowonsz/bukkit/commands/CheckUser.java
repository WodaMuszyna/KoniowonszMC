package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.User;

public class CheckUser implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s.isOp()){
            if(a.length == 0){
                s.sendMessage("§cNie podales nazwy gracza!");
                return true;
            }
            User u = Main.getUserManager().getUserFromDB(a[0]);
            if(u == null){
                s.sendMessage("§cNie znaleziono gracza!");
                return true;
            }
            s.sendMessage("§aInformacje o graczu: §b" + u.getName());
            s.sendMessage("§6Adres IP: §a" + u.getIp());
            s.sendMessage("§6Ostatnio online: §a" + u.getLastOnline().toString());
            s.sendMessage("§6Ostatnia lokacja: §a" + u.getLastLocation());
        }
        return false;
    }
}
