package me.rismose.mhr;

import me.rismose.mhr.command.HuntedCommand;
import me.rismose.mhr.command.ManHuntCommand;
import me.rismose.mhr.command.AssasinCommand;
import me.rismose.mhr.event.Events;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class MHR extends JavaPlugin {

    @Override
    public void onEnable() {
        TeamManager manager = new TeamManager(this);
        PlayerData playerData = new PlayerData();
        Config config = new Config(this);

        getServer().getPluginManager().registerEvents(new Events(playerData, config), this);

        Optional.ofNullable(getCommand("assassin"))
                .ifPresent(c -> c.setExecutor(new AssasinCommand(this, manager, playerData, config)));
        Optional.ofNullable(getCommand("hunted"))
                .ifPresent(c -> c.setExecutor(new HuntedCommand(this, manager, playerData)));
        Optional.ofNullable(getCommand("manhunt"))
                .ifPresent(c -> c.setExecutor(new ManHuntCommand(playerData)));

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Worker(this, playerData, config), 1, 1);

        getLogger().info("MHR plugin started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MHR plugin has stopped.");
    }
}
