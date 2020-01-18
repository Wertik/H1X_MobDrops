package space.devport.wertik.mobdrops.system;

import org.bukkit.configuration.ConfigurationSection;
import space.devport.wertik.mobdrops.Configuration;
import space.devport.wertik.mobdrops.ItemBuilder;
import space.devport.wertik.mobdrops.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurationManager {

    private Configuration config;

    private MobConfiguration defaults;
    private HashMap<String, MobConfiguration> configurations = new HashMap<>();

    public ConfigurationManager(Configuration config) {
        this.config = config;
    }

    public void loadConfigurations() {
        configurations.clear();

        // Load default values
        defaults = loadConfiguration("Defaults");

        // Load the rest
        for (String name : config.getYaml().getConfigurationSection("Mobs").getKeys(false)) {
            configurations.put(name, loadConfiguration("Mobs-" + name));
        }
    }

    public MobConfiguration loadConfiguration(String path) {
        ConfigurationSection cfgSec = config.getYaml().getConfigurationSection(path.replace("-", "."));

        MobConfiguration mobConfiguration = new MobConfiguration();

        mobConfiguration.setDisableVanillaDrops(cfgSec.getBoolean("disable-vanilla-drops", false));
        mobConfiguration.setToInventoryVanilla(cfgSec.getBoolean("to-inventory-vanilla", false));
        mobConfiguration.setPlayerKill(cfgSec.getBoolean("player-kill", true));
        mobConfiguration.setToInventory(cfgSec.getBoolean("to-inventory", false));

        List<ItemBuilder> drops = new ArrayList<>();

        if (cfgSec.contains("drops"))
            for (String dropName : cfgSec.getConfigurationSection("drops").getKeys(false)) {
                ConfigurationSection dropSec = cfgSec.getConfigurationSection("drops." + dropName);

                ItemBuilder itemBuilder = ItemBuilder.loadBuilder(config.getYaml(), dropSec.getCurrentPath());
                drops.add(itemBuilder);
            }

        mobConfiguration.setDrops(drops);

        return mobConfiguration;
    }

    public MobConfiguration get(String name) {
        return configurations.getOrDefault(name, defaults);
    }

    public HashMap<String, MobConfiguration> getConfigurations() {
        return configurations;
    }
}
