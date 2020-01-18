package space.devport.wertik.mobdrops;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private Material type;
    private short damage = 0;

    private String displayName;

    private int amount;

    private List<String> lore;

    private boolean glow;

    private HashMap<Enchantment, Integer> enchants;

    private List<ItemFlag> flags;

    public ItemBuilder(Material type) {
        this.type = type;
        this.amount = 1;

        lore = new ArrayList<>();
        glow = false;
        enchants = new HashMap<>();
        flags = new ArrayList<>();
    }

    public ItemBuilder(ItemStack item) {
        this.type = item.getType();

        this.damage = (byte) item.getDurability();

        this.amount = item.getAmount();

        this.lore = new ArrayList<>();
        this.enchants = new HashMap<>();
        this.flags = new ArrayList<>();

        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta.hasDisplayName())
                this.displayName = itemMeta.getDisplayName();

            if (itemMeta.hasLore())
                this.lore = itemMeta.getLore();

            if (itemMeta.hasEnchants())
                this.enchants = new HashMap<>(itemMeta.getEnchants());

            this.flags = new ArrayList<>(itemMeta.getItemFlags());
        }
    }

    public static ItemBuilder loadBuilder(FileConfiguration yaml, String path) {
        try {
            ConfigurationSection section = yaml.getConfigurationSection(path);

            String type = section.getString("type");
            Material mat = Material.valueOf(type.toUpperCase());

            short data = section.contains("data") ? (short) section.getInt("data") : 0;

            ItemBuilder b = new ItemBuilder(mat).damage(data);

            if (section.contains("name"))
                b.displayName(section.getString("name"));

            if (section.contains("amount"))
                b.amount(section.getInt("amount"));

            if (section.contains("glow"))
                b.setGlow(section.getBoolean("glow"));

            if (section.contains("lore"))
                b.lore(section.getStringList("lore"));

            return b;
        } catch (NullPointerException e) {
            return new ItemBuilder(Material.STONE).displayName("&cCould not load item").addLine("&7Reason: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return new ItemBuilder(Material.STONE).displayName("&cCould not load item").addLine("&7Reason: &cInvalid material");
        }
    }

    public ItemBuilder parsePAPI(Player p) {
        if (Main.inst.usePapi) {
            if (displayName != null)
                displayName = PlaceholderAPI.setPlaceholders(p, displayName);

            if (lore != null)
                if (!lore.isEmpty()) {
                    List<String> newLore = new ArrayList<>();

                    for (String line : lore)
                        newLore.add(PlaceholderAPI.setPlaceholders(p, line));

                    lore = newLore;
                }
        }

        return this;
    }

    public ItemBuilder parse(String str, String str1) {

        if (displayName != null)
            displayName = displayName.replace(str, str1);

        if (lore != null)
            if (!lore.isEmpty()) {
                List<String> newLore = new ArrayList<>();

                for (String line : lore)
                    newLore.add(line.replace(str, str1));

                lore = newLore;
            }

        return this;
    }

    public ItemBuilder clearEnchants() {
        this.enchants = new HashMap<>();
        return this;
    }

    public ItemBuilder clearFlags() {
        this.flags = new ArrayList<>();
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder damage(short data) {
        this.damage = data;
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        for (String line : lore)
            this.lore.add(line);
        return this;
    }

    public ItemBuilder addLine(String str) {
        lore.add(str);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        flags.add(flag);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder type(Material mat) {
        this.type = mat;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(type, amount, damage);

        ItemMeta meta = item.getItemMeta();

        if (!lore.isEmpty()) {
            // Color it
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                lore.set(i, color(line));
            }

            meta.setLore(lore);
        }

        if (displayName != null)
            meta.setDisplayName(color(displayName));

        if (!enchants.isEmpty()) {
            for (Enchantment enchantment : enchants.keySet()) {
                meta.addEnchant(enchantment, enchants.get(enchantment), true);
            }
        }

        if (!flags.isEmpty()) {
            for (ItemFlag flag : flags)
                meta.addItemFlags(flag);
        }

        if (glow) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        return item;
    }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public Material type() {
        return type;
    }

    public short damage() {
        return damage;
    }

    public String displayName() {
        return displayName;
    }

    public int amount() {
        return amount;
    }

    public List<String> lore() {
        return lore;
    }

    public boolean glow() {
        return glow;
    }

    public HashMap<Enchantment, Integer> enchants() {
        return enchants;
    }

    public List<ItemFlag> flags() {
        return flags;
    }
}
