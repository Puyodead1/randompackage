package me.randomhashtags.randompackage.addon.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.randomhashtags.randompackage.universal.UMaterial;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface Skullable {
    HashMap<String, ItemStack> CACHED_SKULLS = new HashMap<>();
    String getOwner();
    // https://www.spigotmc.org/threads/143323/ , edited by RandomHashTags
    default ItemStack getSkull(String name, List<String> lore, boolean isLegacy) {
        final String skinURL = getOwner();
        if(CACHED_SKULLS.containsKey(skinURL)) {
            return CACHED_SKULLS.get(skinURL);
        }
        final ItemStack head = UMaterial.PLAYER_HEAD_ITEM.getItemStack();
        final SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if(headMeta != null) {
            headMeta.setDisplayName(name);
            headMeta.setLore(lore);
            head.setItemMeta(headMeta);
        }

        if(skinURL.isEmpty()) {
            return head;
        } else if(skinURL.startsWith("http")) {
            final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            final byte[] encoded;
            if(isLegacy) {
                encoded = org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
            } else {
                encoded = org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
            }
            profile.getProperties().put("textures", new Property("textures", new String(encoded)));
            Field profileField = null;
            try {
                profileField = headMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(headMeta, profile);
                profileField.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            headMeta.setOwner(skinURL);
        }
        head.setItemMeta(headMeta);
        CACHED_SKULLS.put(skinURL, head);
        return head;
    }
}
