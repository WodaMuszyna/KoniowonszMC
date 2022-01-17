package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;

public class Koniowonsz implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(!s.isOp()){
            s.sendMessage("Brak uprawnien.");
            return true;
        }
        if(a.length == 0){
            s.sendMessage(Utils.fixColor("&7Dostepne argumenty: &atoggle"));
            return true;
        }
        if(a[0].equalsIgnoreCase("toggle")){
            Main.getInstance().toggleMaintenance();
            s.sendMessage(Utils.fixColor("&7Tryb maintenance zostal " + (Main.getInstance().isMaintenance() ? "&awlaczony" : "&cwylaczony")));
            return true;
        }
        return false;
    }
}
