package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.utils.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.runnables.LosujLootbox;

import java.util.Locale;

public class DajLootboxy implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s.hasPermission("nascraft.dajlootboxy")){
            if(a.length < 3){
                s.sendMessage("Uzycie: /dajlootboxy <nick lub *> <typ/random> <ile>");
                return true;
            }
            if(!a[1].equalsIgnoreCase("random") && Main.getLootboxManager().getLootbox(a[1]) == null){
                s.sendMessage(String.format("Lootbox %s nie istnieje", a[1]));
                return true;
            }
            int ilosc;
            try{
                ilosc = Integer.parseInt(a[2]);
            }catch (NumberFormatException e){
                s.sendMessage("Nieprawidlowa liczba: "+e);
                return true;
            }
            if(a[0].equals("*")){
                if(a[1].equalsIgnoreCase("random")){
                    for(Player pl : Bukkit.getOnlinePlayers()){
                        for(int i=0; i<ilosc; i++) {
                            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new LosujLootbox(pl, Main.getLootboxManager().getLootbox(a[1]), true),
                                    20);
                        }
                    }
                }else {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        Main.getLootboxManager().give(pl, a[1], ilosc);
                    }
                }
                Main.getInstance().getServer().broadcast(Component.text(Utils.fixColor(Main.getInstance().lootboxConfig.broadcastLootboxy
                        .replace("{ILE}", Integer.toString(ilosc))
                        .replace("{LOOTBOXY}", Main.getLootboxManager().odmien(ilosc, 3))
                        .replace("{LOOTBOXY}", Main.getLootboxManager().odmien(ilosc, 0))
                        .replace("{TYP}", a[1].toUpperCase(Locale.ROOT)))));
            }else{
                Player p = Bukkit.getPlayer(a[0]);
                if(p != null){
                    if(a[1].equalsIgnoreCase("random")){
                        for(int i=0; i<ilosc; i++) {
                            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new LosujLootbox(p, Main.getLootboxManager().getLootbox(a[1]), true),
                                    20);
                        }
                    }else {
                        Main.getLootboxManager().give(p, a[1], ilosc);
                    }
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
