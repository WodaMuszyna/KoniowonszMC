package xyz.wodamuszyna.koniowonsz.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import xyz.wodamuszyna.koniowonsz.Main;

public class InventoryListener implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getClickedInventory() == null){
            return;
        }
        if(e.getCurrentItem() == null){
            return;
        }
        if (Main.getInstance().inventories.contains(e.getClickedInventory())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryDragEvent e) {
        if (Main.getInstance().inventories.contains(e.getInventory())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryInteract(InventoryInteractEvent e) {
        if (Main.getInstance().inventories.contains(e.getInventory())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryMoveItem(InventoryMoveItemEvent e) {
        if (Main.getInstance().inventories.contains(e.getSource()) || Main.getInstance().inventories.contains(e.getDestination())) {
            e.setCancelled(true);
        }
    }
}
