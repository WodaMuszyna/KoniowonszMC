package xyz.wodamuszyna.koniowonsz.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RTP implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String l, @NotNull String[] a) {
        if(s instanceof Player){
            Player p = Bukkit.getPlayer(s.getName());
            Block b = p.getWorld().getHighestBlockAt((int) (Math.random() * 7000 - 5000), (int) (Math.random() * 7000 - 5000));
            boolean badblock = true;
            while (badblock){
                if(b.getType().isSolid()){
                    badblock = false;
                }else{
                    b = p.getWorld().getHighestBlockAt((int) (Math.random() * 7000 - 5000), (int) (Math.random() * 7000 - 5000));
                }
            }
            p.teleport(b.getLocation());
            s.sendMessage("§aPrzeteleportowano na losowe koordynaty!");
        }
        return false;
    }
}
