package me.brand0n_.hoverstats.Commands;

import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HoverStatsCommand implements CommandExecutor {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        // If command sent is "mentionplayer"
        if (cmd.getName().equalsIgnoreCase("hoverstats")) {
            // Check if the command is the only message
            if (args.length == 0) {
                // Check if sender is a player or if its console
                if (sender instanceof Player) {
                    // Get player
                    Player p = (Player) sender;
                    // Check if player is able to use this command
                    if (sender.hasPermission("hoverstats.help") || sender.isOp()) {
                        // Send player help message
                        plugin.messages.sendHelpMessage(p, "Messages.Help");
                        return true;
                    } else {
                        sender.sendMessage(plugin.messages.noPermissions(sender));
                        return false;
                    }
                } else {
                    // Send console help message
                    plugin.messages.sendHelpMessageConsole("Messages.Help");
                }
                return true;
            }
            // Check if the player enters "/mentionplayer help"
            if (args[0].equalsIgnoreCase("help")) {
                // Check if player is able to use this command
                if (sender.hasPermission("hoverstats.help") || sender.isOp()) {
                    // Check if sender is a player or if its console
                    if (sender instanceof Player) {
                        // Get player
                        Player p = (Player) sender;
                        // Send player help message
                        plugin.messages.sendHelpMessage(p, "Messages.Help");
                    } else {
                        // Send console help message
                        plugin.messages.sendHelpMessageConsole("Messages.Help");
                    }
                    return true;
                } else {
                    sender.sendMessage(plugin.messages.noPermissions(sender));
                    return false;
                }
            }
            // Check if the player type /mentionplayer reload or /mentionplayer rl
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                // Check if player is able to use this command
                if (sender.hasPermission("hoverstats.reload") || sender.isOp()) {
                    // Reload the plugin
                    plugin.reloadPlugin(sender);
                    return true;
                } else {
                    sender.sendMessage(plugin.messages.noPermissions(sender));
                    return false;
                }
            }
        }
        return false;
    }
}
