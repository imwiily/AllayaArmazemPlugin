package com.example.armazem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import com.example.armazem.WarehouseManager;
import com.example.armazem.ArmazemPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;

public class InventoryCloseListener implements Listener {
    private final WarehouseManager warehouseManager;

    public InventoryCloseListener(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (!title.startsWith("§6Armazém")) return;
        int warehouseNumber;
        try {
            String[] parts = title.split(" ");
            warehouseNumber = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return;
        }
        Inventory topInventory = event.getView().getTopInventory();
        Player player = (Player) event.getPlayer();
        // Obtém os conteúdos do inventário e substitui null por ItemStack de AIR
        ItemStack[] contents = topInventory.getContents();
        List<ItemStack> updatedItems = new ArrayList<>();
        for (ItemStack item : contents) {
            if (item == null) {
                updatedItems.add(new ItemStack(Material.AIR));
            } else {
                updatedItems.add(item);
            }
        }
        warehouseManager.setItems(player.getUniqueId(), warehouseNumber, updatedItems);
        ArmazemPlugin plugin = warehouseManager.getPlugin();
        String message = plugin.getConfig().getString("messages.warehouseUpdated", "Seu armazém {number} foi atualizado!");
        message = message.replace("{number}", String.valueOf(warehouseNumber));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
