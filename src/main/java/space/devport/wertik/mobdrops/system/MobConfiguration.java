package space.devport.wertik.mobdrops.system;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import space.devport.wertik.mobdrops.ItemBuilder;
import space.devport.wertik.mobdrops.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MobConfiguration {

    private List<ItemBuilder> drops = new ArrayList<>();

    private boolean toInventory = false;
    private boolean toInventoryVanilla = false;
    private boolean playerKill = true;
    private boolean disableVanillaDrops = false;

    public List<ItemStack> buildDrops(Player player, Location eLocation) {
        List<ItemStack> builtItems = new ArrayList<>();

        for (ItemBuilder itemB : drops) {
            if (player != null)
                itemB.parse("%player%", player.getName()).parsePAPI(player);

            DecimalFormat format = new DecimalFormat("#.##");

            builtItems.add(itemB
                    .parse("%prefix%", Main.inst.cO.getPrefix())
                    .parse("%loc_x%", format.format(eLocation.getX()))
                    .parse("%loc_y%", format.format(eLocation.getY()))
                    .parse("%loc_z%", format.format(eLocation.getZ()))
                    .build());
        }

        return builtItems;
    }

    public List<ItemBuilder> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemBuilder> drops) {
        this.drops = drops;
    }

    public boolean isToInventory() {
        return toInventory;
    }

    public void setToInventory(boolean toInventory) {
        this.toInventory = toInventory;
    }

    public boolean isPlayerKill() {
        return playerKill;
    }

    public void setPlayerKill(boolean playerKill) {
        this.playerKill = playerKill;
    }

    public boolean isDisableVanillaDrops() {
        return disableVanillaDrops;
    }

    public void setDisableVanillaDrops(boolean disableVanillaDrops) {
        this.disableVanillaDrops = disableVanillaDrops;
    }

    public boolean isToInventoryVanilla() {
        return toInventoryVanilla;
    }

    public void setToInventoryVanilla(boolean toInventoryVanilla) {
        this.toInventoryVanilla = toInventoryVanilla;
    }
}
