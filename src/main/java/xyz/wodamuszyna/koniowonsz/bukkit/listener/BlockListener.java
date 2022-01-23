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
import xyz.wodamuszyna.koniowonsz.bukkit.config.LootboxConfig;
import xyz.wodamuszyna.koniowonsz.bukkit.runnables.LosujLootbox;

import java.util.Map;


public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        for(Map.Entry<String, LootboxConfig> m : Main.getInstance().lootboxy.entrySet()) {
            if (e.getItemInHand().getType().equals(m.getValue().lootbox.getType()) && e.getItemInHand().getItemMeta().equals(m.getValue().lootbox.getItemMeta())) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                ItemStack inHand = p.getInventory().getItemInMainHand();
                if (inHand.getAmount() > 1) {
                    inHand.setAmount(inHand.getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                if (m.getValue().losowanie != null) {
                    p.sendMessage(Utils.fixColor(m.getValue().losowanie));
                }
                if (m.getValue().broadcastLootbox != null) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.sendMessage(Utils.fixColor(m.getValue().broadcastLootbox.replace("{NICK}", p.getName())));
                    }
                }
                Location loc = e.getBlock().getLocation();
                Firework firework = (Firework) loc.getWorld().spawn(loc, Firework.class);
                FireworkMeta data = firework.getFireworkMeta();
                data.setPower(m.getValue().mocFajerwerki);
                data.addEffects(m.getValue().efekty);
                firework.setFireworkMeta(data);
                Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new LosujLootbox(e.getPlayer(), m.getValue(), false),
                        m.getValue().opoznienie);
            }
        }
    }
}
