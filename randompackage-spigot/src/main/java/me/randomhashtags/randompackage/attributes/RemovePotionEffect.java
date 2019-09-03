package me.randomhashtags.randompackage.attributes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;

public class RemovePotionEffect extends AbstractEventAttribute {
    @Override
    public void execute(HashMap<Entity, String> recipientValues) {
        for(Entity e : recipientValues.keySet()) {
            if(e instanceof LivingEntity) {
                ((LivingEntity) e).removePotionEffect(getPotionEffectType(recipientValues.get(e)));
            }
        }
    }
}
