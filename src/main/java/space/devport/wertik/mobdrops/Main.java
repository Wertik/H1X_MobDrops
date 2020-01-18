package space.devport.wertik.mobdrops;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import space.devport.wertik.mobdrops.commands.MobDropsCommand;
import space.devport.wertik.mobdrops.listeners.EntityDeathListener;
import space.devport.wertik.mobdrops.system.ConfigurationManager;

public class Main extends JavaPlugin {

    public static Main inst;

    public ConsoleOutput cO;
    private Configuration config;

    private ConfigurationManager manager;

    public boolean usePapi = false;

    @Override
    public void onEnable() {
        inst = this;

        cO = new ConsoleOutput(this);
        config = new Configuration(this, "config");

        cO.setDebug(config.getYaml().getBoolean("debug-enabled"));
        cO.setPrefix(config.getColored("plugin-prefix"));

        manager = new ConfigurationManager(config);
        manager.loadConfigurations();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            usePapi = true;

        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getCommand("mobdrops").setExecutor(new MobDropsCommand());
    }

    public void reload(CommandSender s) {
        long start = System.currentTimeMillis();

        cO.setReloadSender(s);

        config.reload();
        cO.setDebug(config.getYaml().getBoolean("debug-enabled"));
        cO.setPrefix(config.getColored("plugin-prefix"));

        manager.loadConfigurations();
        cO.info("Loaded " + manager.getConfigurations().size() + " mob configuration(s)..");

        usePapi = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        cO.setReloadSender(null);

        s.sendMessage("§aDone.. reload took " + (System.currentTimeMillis() - start) + "ms.");
    }

    @Override
    public void onDisable() {

    }

    public Configuration getCfg() {
        return config;
    }

    public ConfigurationManager getManager() {
        return manager;
    }
}
