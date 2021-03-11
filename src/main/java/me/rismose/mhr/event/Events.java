package me.rismose.mhr.event;

import me.rismose.mhr.Config;
import me.rismose.mhr.PlayerData;
import me.rismose.mhr.model.ManHuntRole;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;


public class Events implements Listener {

    private final PlayerData playerData;
    private final Config config;

    public Events(PlayerData playerData, Config config) {
        this.playerData = playerData;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER) return;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        if (playerData.isFrozen(attacker)) {
            event.setCancelled(true);
            return;
        }

        // prevent hunted from attacking assassin
        if (playerData.isFrozen(player) && playerData.getRole(attacker) == ManHuntRole.HUNTED) {
            event.setCancelled(true);
            return;
        }

        if (
                config.isInstaKill() &&
                        playerData.getRole(attacker) == ManHuntRole.ASSASSIN &&
                        playerData.getRole(player) == ManHuntRole.HUNTED
        ) {
            player.damage(999);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (playerData.isFrozen(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (config.giveCompass() && playerData.getRole(player) == ManHuntRole.ASSASSIN)
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        playerData.reset(event.getPlayer());
    }
}
