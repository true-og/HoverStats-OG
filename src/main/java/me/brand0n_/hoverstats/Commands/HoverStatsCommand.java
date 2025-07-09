package me.brand0n_.hoverstats.Commands;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Messages;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HoverStatsCommand implements CommandExecutor {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // Check if the argument is 1 or greater
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                // Check if the player has the permission to reload the plugin
                if (Permissions.hasPermission(sender, "hoverstats.reload")) {
                    // Reload the plugin
                    plugin.reloadPlugin(sender);
                    // Exit the code, all work is done
                    return true;
                }
                // Tell the player they don't have permissions to run this command
                Messages.noPermissions(sender);
                return false;
            }
            // Check if the player is trying to get the plugin version
            if (args[0].equalsIgnoreCase("version")
                    || args[0].equalsIgnoreCase("ver")
                    || args[0].equalsIgnoreCase("v")) {
                if (Permissions.hasPermission(sender, "hoverstats.version")) {
                    // Tell the player what version their on
                    Messages.sendVersionInfo(sender);
                    // Exit the code, all work is done
                    return true;
                }
            }
        }
        // Check if the player has permission to see the help message
        if (Permissions.hasPermission(sender, "hoverstats.help")) {
            // Send the player the help message
            Messages.sendHelpMessage(sender);
            // Exit the code, all work is done
            return true;
        }
        // Tell the player they don't have permissions to run this command
        Messages.noPermissions(sender);
        return false;
    }
}
