package com.example.armazem.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.example.armazem.WarehouseManager;
import com.example.armazem.ArmazemPlugin;
import org.bukkit.ChatColor;
import java.util.List;

public class VerArmazemCommand implements CommandExecutor {
    private final WarehouseManager warehouseManager;

    public VerArmazemCommand(WarehouseManager warehouseManager) {
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
        if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.usageVerArmazem", "Uso: /verarmazem <jogador> <número do armazém>")));
            return true;
        }
        if (!player.hasPermission("allaya.armazem.ver")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.noPermissionVer", "Você não tem permissão para ver os armazéns dos jogadores.")));
            return true;
        }
        String targetName = args[0];
        int warehouseNumber;
        try {
            warehouseNumber = Integer.parseInt(args[1]);
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
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null || !target.hasPlayedBefore()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.playerNotFound", "Jogador não encontrado.")));
            return true;
        }
        List<ItemStack> items = warehouseManager.getItems(target.getUniqueId(), warehouseNumber);
        String baseTitle = plugin.getConfig().getString("warehouse.title", "&6Armazém");
        String title = ChatColor.translateAlternateColorCodes('&', baseTitle) + " " + warehouseNumber + " de " + target.getName();
        Inventory gui = Bukkit.createInventory(null, 54, title);
        for (int i = 0; i < items.size() && i < 54; i++) {
            gui.setItem(i, items.get(i));
        }
        player.openInventory(gui);
        return true;
    }
}
