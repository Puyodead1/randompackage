package me.randomhashtags.randompackage.utils.addons;

import me.randomhashtags.randompackage.addons.Pet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class FilePet extends RPAddon implements Pet {
    private ItemStack item;
    public FilePet(File f) {
        load(f);
        addPet(getIdentifier(), this);
    }
    public String getIdentifier() { return getYamlName(); }

    public int getCooldownSlot() { return get("{COOLDOWN}"); }
    public int getExpSlot() { return get("{EXP}"); }
    private int get(String input) {
        final List<String> l = getItem().getItemMeta().getLore();
        for(int i = 0; i < l.size(); i++) {
            if(l.get(i).contains(input)) {
                return i;
            }
        }
        return -1;
    }
    public TreeMap<Integer, Long> getCooldowns() {
        final TreeMap<Integer, Long> a = new TreeMap<>();
        final ConfigurationSection c = yml.getConfigurationSection("settings.cooldowns");
        if(c != null) {
            for(String s : c.getKeys(false)) {
                a.put(Integer.parseInt(s), yml.getLong("settings.cooldowns." + s));
            }
        }
        return a;
    }
    public ItemStack getItem() {
        if(item == null) item = api.d(yml, "item");
        return item.clone();
    }
    public TreeMap<Integer, Long> getRequiredXp() {
        final TreeMap<Integer, Long> a = new TreeMap<>();
        final ConfigurationSection c = yml.getConfigurationSection("settings.exp to level");
        if(c != null) {
            for(String s : c.getKeys(false)) {
                a.put(Integer.parseInt(s), yml.getLong("settings.exp to level." + s));
            }
        }
        return a;
    }
    public List<String> getAttributes() { return yml.getStringList("attributes"); }
}
