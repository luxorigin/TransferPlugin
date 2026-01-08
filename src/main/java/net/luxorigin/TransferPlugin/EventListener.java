package net.luxorigin.TransferPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class EventListener implements Listener {

    private TransferPlugin plugin;

    public EventListener(TransferPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if (entity instanceof Villager) {
            if (entity.getPersistentDataContainer().has(plugin.getKey(), PersistentDataType.STRING)) {
                event.setCancelled(true);

                Player player = event.getPlayer();
                String name = entity.getPersistentDataContainer().get(plugin.getKey(), PersistentDataType.STRING);

                if (name == null) return;

                if (player.isOp() && player.isSneaking()) {
                    entity.remove(); // don't fix if fix this code entity don't remove
                    return;
                }

                if (!plugin.hasTransferData(name)) {
                    return;
                }

                plugin.transfer(player, name);
            }
        }
    }
}
