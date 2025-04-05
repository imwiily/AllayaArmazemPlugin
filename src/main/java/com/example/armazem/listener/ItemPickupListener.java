package com.example.armazem.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import com.example.armazem.WarehouseManager;
import com.example.armazem.util.PlotSquaredAPI;
import com.example.armazem.ArmazemPlugin;
import org.bukkit.ChatColor;

public class ItemPickupListener implements Listener {
    private final WarehouseManager warehouseManager;

    public ItemPickupListener(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Location loc = event.getItem().getLocation();

        if (PlotSquaredAPI.isInPlayerPlot(player, loc)) {
            event.setCancelled(true);
            ItemStack item = event.getItem().getItemStack();
            warehouseManager.addItem(player.getUniqueId(), 1, item);
            ArmazemPlugin plugin = warehouseManager.getPlugin();
            String message = plugin.getConfig().getString("messages.autoStore", "Item armazenado automaticamente no seu armazém 1!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            event.getItem().remove();
        }
    }
}
