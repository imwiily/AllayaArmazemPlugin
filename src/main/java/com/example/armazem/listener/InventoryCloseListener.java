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
        ArmazemPlugin plugin = warehouseManager.getPlugin();
        // Obtém o título base conforme configurado
        String baseTitleRaw = plugin.getConfig().getString("warehouse.title", "&6Armazém");
        String baseTitle = ChatColor.translateAlternateColorCodes('&', baseTitleRaw);
        String title = event.getView().getTitle();

        // Verifica se o título inicia com o baseTitle e se segue o formato esperado
        if (!title.startsWith(baseTitle)) return;

        // Espera o formato: "<baseTitle> - <número>" ou "<baseTitle> - <número> de <jogador>"
        String[] parts = title.split(" - ");
        if (parts.length < 2) return;

        int warehouseNumber;
        try {
            warehouseNumber = Integer.parseInt(parts[1].split(" ")[0]);
        } catch (Exception e) {
            return;
        }

        Inventory topInventory = event.getView().getTopInventory();
        Player player = (Player) event.getPlayer();
        // Salva o inventário preservando todos os slots (substitui null por AIR)
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

        String message = plugin.getConfig().getString("messages.warehouseUpdated", "Seu armazém {number} foi atualizado!");
        message = message.replace("{number}", String.valueOf(warehouseNumber));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
