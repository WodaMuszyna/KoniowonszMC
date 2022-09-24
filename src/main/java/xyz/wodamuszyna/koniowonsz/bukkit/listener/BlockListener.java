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
import xyz.wodamuszyna.koniowonsz.utils.Utils;
import xyz.wodamuszyna.koniowonsz.bukkit.objects.Lootbox;
import xyz.wodamuszyna.koniowonsz.bukkit.runnables.LosujLootbox;


public class BlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        for(Lootbox l : Main.getLootboxManager().getLootboxy()) {
            if (e.getItemInHand().getType().equals(l.getConfig().lootbox.getType()) && e.getItemInHand().getItemMeta().equals(l.getConfig().lootbox.getItemMeta())) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                ItemStack inHand = p.getInventory().getItemInMainHand();
                if (inHand.getAmount() > 1) {
                    inHand.setAmount(inHand.getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                if (l.getConfig().losowanie != null) {
                    p.sendMessage(Utils.fixColor(l.getConfig().losowanie));
                }
                if (l.getConfig().broadcastLootbox != null) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.sendMessage(Utils.fixColor(l.getConfig().broadcastLootbox.replace("{NICK}", p.getName())));
                    }
                }
                Location loc = e.getBlock().getLocation();
                Firework firework = (Firework) loc.getWorld().spawn(loc, Firework.class);
                FireworkMeta data = firework.getFireworkMeta();
                data.setPower(l.getConfig().mocFajerwerki);
                data.addEffects(l.getConfig().efekty);
                firework.setFireworkMeta(data);
                Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new LosujLootbox(e.getPlayer(), l, false),
                        l.getConfig().opoznienie);
            }
        }
    }
}
