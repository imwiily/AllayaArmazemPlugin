package com.example.armazem;

import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class WarehouseManager {
    private final ArmazemPlugin plugin;
    private final YamlConfiguration dataConfig;

    public WarehouseManager(ArmazemPlugin plugin, YamlConfiguration dataConfig) {
        this.plugin = plugin;
        this.dataConfig = dataConfig;
    }

    public ArmazemPlugin getPlugin() {
        return plugin;
    }

    @SuppressWarnings("unchecked")
    public void addItem(UUID playerUUID, int warehouseNumber, ItemStack item) {
        String path = "players." + playerUUID.toString() + ".warehouses." + warehouseNumber;
        List<ItemStack> items = (List<ItemStack>) dataConfig.getList(path);
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        dataConfig.set(path, items);
        plugin.saveData();
    }

    @SuppressWarnings("unchecked")
    public List<ItemStack> getItems(UUID playerUUID, int warehouseNumber) {
        String path = "players." + playerUUID.toString() + ".warehouses." + warehouseNumber;
        List<ItemStack> items = (List<ItemStack>) dataConfig.getList(path);
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public void removeItem(UUID playerUUID, int warehouseNumber, int index) {
        String path = "players." + playerUUID.toString() + ".warehouses." + warehouseNumber;
        List<ItemStack> items = (List<ItemStack>) dataConfig.getList(path);
        if (items != null && index >= 0 && index < items.size()) {
            items.remove(index);
            dataConfig.set(path, items);
            plugin.saveData();
        }
    }

    public void setItems(UUID playerUUID, int warehouseNumber, List<ItemStack> items) {
        String path = "players." + playerUUID.toString() + ".warehouses." + warehouseNumber;
        dataConfig.set(path, items);
        plugin.saveData();
    }
}
