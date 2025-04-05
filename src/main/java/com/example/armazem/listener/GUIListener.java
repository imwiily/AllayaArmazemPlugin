package com.example.armazem.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.example.armazem.WarehouseManager;

public class GUIListener implements Listener {
    private final WarehouseManager warehouseManager;

    public GUIListener(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        // Verifica se a GUI é de um armazém (título configurável, por exemplo "§6Armazém 1" ou "§6Armazém 1 de Jogador")
        if (!title.startsWith("§6Armazém")) {
            return;
        }

        event.setCancelled(true); // Cancela ações padrão

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        // Extrai o número do armazém a partir do título.
        int warehouseNumber;
        try {
            String[] parts = title.split(" ");
            warehouseNumber = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return;
        }

        // Se o clique ocorreu na parte superior (GUI do armazém), trata como remoção de item
        if (clickedInventory.equals(event.getView().getTopInventory())) {
            int slot = event.getRawSlot();
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                // Chama removeItem com três parâmetros
                warehouseManager.removeItem(player.getUniqueId(), warehouseNumber, slot);
                player.sendMessage("§aItem removido do armazém!");
                player.closeInventory();
            }
        }
        // Se o clique ocorreu no inventário do jogador (parte inferior), trata como adição de item
        else if (clickedInventory.equals(player.getInventory())) {
            ItemStack cursorItem = event.getCursor();
            if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                warehouseManager.addItem(player.getUniqueId(), warehouseNumber, cursorItem);
                player.sendMessage("§aItem adicionado ao armazém!");
                event.setCursor(null);
                player.updateInventory();
            }
        }
    }
}
