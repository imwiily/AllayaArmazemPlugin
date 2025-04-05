package com.example.armazem;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class ArmazemPlugin extends JavaPlugin {
    private WarehouseManager warehouseManager;
    private File dataFile;
    private YamlConfiguration dataConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("ArmazemPlugin habilitado!");
        loadData();

        warehouseManager = new WarehouseManager(this, dataConfig);

        // Registra os comandos
        getCommand("armazem").setExecutor(new com.example.armazem.command.ArmazemCommand(warehouseManager));
        getCommand("verarmazem").setExecutor(new com.example.armazem.command.VerArmazemCommand(warehouseManager));

        // Registra os listeners
        getServer().getPluginManager().registerEvents(new com.example.armazem.listener.ItemPickupListener(warehouseManager), this);
        getServer().getPluginManager().registerEvents(new com.example.armazem.listener.InventoryCloseListener(warehouseManager), this);
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("ArmazemPlugin desabilitado!");
    }

    public void loadData() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WarehouseManager getWarehouseManager() {
        return warehouseManager;
    }
}
