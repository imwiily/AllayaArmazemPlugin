package com.example.armazem.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import com.example.armazem.WarehouseManager;
import com.example.armazem.ArmazemPlugin;
import org.bukkit.ChatColor;
import java.util.List;

public class ArmazemCommand implements CommandExecutor {
    private final WarehouseManager warehouseManager;

    public ArmazemCommand(WarehouseManager warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ArmazemPlugin plugin = warehouseManager.getPlugin();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.notAPlayer", "Apenas jogadores podem usar esse comando.")));
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.usageArmazem", "Uso: /armazem <número do armazém> (1 a 10)")));
            return true;
        }
        int warehouseNumber;
        try {
            warehouseNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.invalidWarehouseNumber", "Número do armazém inválido.")));
            return true;
        }
        if (warehouseNumber < 1 || warehouseNumber > 10) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.warehouseNumberRange", "Número do armazém deve ser entre 1 e 10.")));
            return true;
        }
        if (!player.hasPermission("allaya.armazem." + warehouseNumber)) {
            String msg = plugin.getConfig().getString("messages.noPermission", "Você não tem permissão para acessar o armazém {number}.");
            msg = msg.replace("{number}", String.valueOf(warehouseNumber));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }
        List<ItemStack> items = warehouseManager.getItems(player.getUniqueId(), warehouseNumber);
        String baseTitle = plugin.getConfig().getString("warehouse.title", "&6Armazém");
        String title = ChatColor.translateAlternateColorCodes('&', baseTitle) + " " + warehouseNumber;
        Inventory gui = Bukkit.createInventory(null, 54, title);
        for (int i = 0; i < items.size() && i < 54; i++) {
            // Se o item for AIR, tratamos como slot vazio (null)
            ItemStack item = items.get(i);
            if (item != null && item.getType() == Material.AIR) {
                gui.setItem(i, null);
            } else {
                gui.setItem(i, item);
            }
        }
        player.openInventory(gui);
        return true;
    }
}
