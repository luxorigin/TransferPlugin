package net.luxorigin.TransferPlugin.entity;

import net.luxorigin.TransferPlugin.TransferPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;

public class TransferEntity {

    private final TransferPlugin plugin;

    public TransferEntity(TransferPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawn(Player player, String name) {
        Villager villager = (Villager) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);

        villager.setAI(false);
        villager.setGravity(false);
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setSilent(true);
        villager.setRemoveWhenFarAway(false);
        villager.setCanPickupItems(false);
        villager.setCustomNameVisible(true);
        villager.setCustomName("§aMove: " + name);

        villager.getPersistentDataContainer().set(
                plugin.getKey(),
                PersistentDataType.STRING,
                name.toLowerCase()
        );
    }
}
