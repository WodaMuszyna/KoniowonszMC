package xyz.wodamuszyna.koniowonsz.bukkit.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.wodamuszyna.koniowonsz.Main;
import xyz.wodamuszyna.koniowonsz.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.runnables.LosujLootbox;


public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType().equals(Main.getInstance().lootboxConfig.lootbox.getType()) && e.getItemInHand().getItemMeta().equals(Main.getInstance().lootboxConfig.lootbox.getItemMeta())) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            ItemStack inHand = p.getInventory().getItemInMainHand();
            if (inHand.getAmount() > 1) {
                inHand.setAmount(inHand.getAmount() - 1);
            } else {
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            if (Main.getInstance().lootboxConfig.losowanie != null) {
                p.sendMessage(Utils.fixColor(Main.getInstance().lootboxConfig.losowanie));
            }
            if (Main.getInstance().lootboxConfig.broadcastLootbox != null) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.sendMessage(Utils.fixColor(Main.getInstance().lootboxConfig.broadcastLootbox.replace("{NICK}", p.getName())));
                }
            }
            Location loc = e.getBlock().getLocation();
            Firework firework = (Firework)loc.getWorld().spawn(loc, Firework.class);
            FireworkMeta data = firework.getFireworkMeta();
            data.setPower(Main.getInstance().lootboxConfig.mocFajerwerki);
            data.addEffects(Main.getInstance().lootboxConfig.efekty);
            firework.setFireworkMeta(data);
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new LosujLootbox(e.getPlayer()),
                    Main.getInstance().lootboxConfig.opoznienie);
        }
    }
}
