package space.devport.wertik.mobdrops.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.mobdrops.Main;
import space.devport.wertik.mobdrops.system.MobConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        // Edit drops
        if (!Main.inst.getCfg().getYaml().getStringList("worlds").contains(e.getEntity().getWorld().getName()))
            return;

        // get configuration
        MobConfiguration mobCfg = Main.inst.getManager().get(e.getEntityType().toString());

        // add drop items/give them to killer
        boolean playerKilled = e.getEntity().getKiller() != null;

        // Has to be killed by a player
        if (!playerKilled && mobCfg.isPlayerKill())
            return;

        // Vanilla drops
        if (mobCfg.isDisableVanillaDrops())
            e.getDrops().clear();

        List<ItemStack> toInventory = new ArrayList<>();
        // Drops
        List<ItemStack> drops = mobCfg.buildDrops(e.getEntity().getKiller(), e.getEntity().getLocation());

        // To inventory
        if (mobCfg.isToInventoryVanilla()) {
            toInventory.addAll(e.getDrops());
            e.getDrops().clear();
        }

        if (mobCfg.isToInventory())
            toInventory.addAll(drops);
        else
            e.getDrops().addAll(drops);

        if (playerKilled)
            for (ItemStack item : toInventory)
                e.getEntity().getKiller().getInventory().addItem(item);
    }
}
