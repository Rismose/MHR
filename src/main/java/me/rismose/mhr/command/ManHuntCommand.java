package me.rismose.mhr.command;
import me.rismose.mhr.PlayerData;
import me.rismose.mhr.model.ManHuntRole;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ManHuntCommand implements CommandExecutor {

    private final PlayerData playerData;

    public ManHuntCommand(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder result = new StringBuilder(ChatColor.translateAlternateColorCodes('&',"&9&lManHuntReloaded\n\n"));

        String Hunted = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.HUNTED));
        result.append(ChatColor.translateAlternateColorCodes('&',"&a&lHunted:&e\n\n")).append(Hunted);

        String assassins = formatPlayerList(playerData.getPlayersByRole(ManHuntRole.ASSASSIN));
        result.append(ChatColor.translateAlternateColorCodes('&',"&c&lAssassins:&e\n")).append(assassins);

        sender.sendMessage(result.toString());

        return true;
    }

    private String formatPlayerList(List<Player> players) {
        return players.stream()
                .map(HumanEntity::getName)
                .collect(Collectors.joining("\n"));
    }
}
