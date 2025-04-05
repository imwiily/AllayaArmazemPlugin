package com.example.armazem.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import com.example.armazem.WarehouseManager;
import com.example.armazem.ArmazemPlugin;
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

        // Recupera a lista de itens armazenados para o armazém do jogador
        List<ItemStack> items = warehouseManager.getItems(player.getUniqueId(), warehouseNumber);

        // Constrói o título completo da GUI usando o título base configurado e o número do armazém
        String baseTitleRaw = plugin.getConfig().getString("warehouse.title", "&6Armazém");
        String baseTitle = ChatColor.translateAlternateColorCodes('&', baseTitleRaw);
        String title = baseTitle + " - " + warehouseNumber;

        Inventory gui = Bukkit.createInventory(null, 54, title);

        // Preenche a GUI com os itens armazenados, preservando as posições
        for (int i = 0; i < items.size() && i < 54; i++) {
            ItemStack item = items.get(i);
            // Se o item for do tipo AIR, deixa o slot vazio (null)
            if (item != null && item.getType() == org.bukkit.Material.AIR) {
                gui.setItem(i, null);
            } else {
                gui.setItem(i, item);
            }
        }

        player.openInventory(gui);
        return true;
    }
}
