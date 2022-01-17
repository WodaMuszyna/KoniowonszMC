package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;

public class DajLootboxy implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s.hasPermission("nascraft.dajlootboxy")){
            if(a.length < 2){
                s.sendMessage("Uzycie: /dajlootboxy <nick lub *> <ile>");
                return true;
            }
            int ilosc;
            try{
                ilosc = Integer.parseInt(a[1]);
            }catch (NumberFormatException e){
                s.sendMessage("Nieprawidlowa liczba: "+e);
                return true;
            }
            if(a[0].equals("*")){
                for(Player pl : Bukkit.getOnlinePlayers()){
                    Main.getLootboxManager().give(pl, ilosc);
                }
                Main.getInstance().getServer().broadcast(Component.text(Utils.fixColor(Main.getInstance().lootboxConfig.broadcastLootboxy
                        .replace("{ILE}", Integer.toString(ilosc))
                        .replace("{LOOTBOXY}", Main.getLootboxManager().odmien(ilosc, 3))
                        .replace("{LOOTBOXY}", Main.getLootboxManager().odmien(ilosc, 0)))));
            }else{
                Player p = Bukkit.getPlayer(a[0]);
                if(p != null){
                    Main.getLootboxManager().give(p, ilosc);
                }else{
                    s.sendMessage(Utils.fixColor("&cNie ma takiego gracza!"));
                }
            }
        }else{
            s.sendMessage("Brak uprawnien.");
            return true;
        }
        return false;
    }
}
