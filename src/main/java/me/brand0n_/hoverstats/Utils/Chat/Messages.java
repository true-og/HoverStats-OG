package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Messages {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void sendHelpMessage(CommandSender sender) {
        List<String> path = plugin.getConfig().getStringList("Messages.Help");
        for (String MESSAGE : path) {
            if (sender instanceof Player) {
                sender.sendMessage(Placeholders.addPlaceholders((Player) sender, MESSAGE));
                continue;
            }
             Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(MESSAGE));
        }
    }

    public static void reloaded(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Reloaded", "%success% %pluginname%'s &7config has successfully reloaded!");
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage(Placeholders.formatPlaceholders(path));
    }

    public static void noPermissions(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.No Permissions", "%error% &7You don't have permission to run this command!");
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;
        }
         Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));
    }

    public static String papiHookSuccess() {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.PAPI Hook", "&aFound PlaceholderAPI, hooking into it.");
        return Placeholders.formatPlaceholders(path);
    }
    public static String papiHookFailed() {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.PAPI Hook", "&aCouldn't find PlaceholderAPI, all placeholders from PAPI won't work.");
        return Placeholders.formatPlaceholders(path);
    }

    public static void sendVersionInfo(CommandSender sender) {
        // Define the message being sent
        String message = "\n&b" + plugin.getName() + " &8[&a" + plugin.getDescription().getVersion() + "&8]\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n" +
                "  &7Author: &a" + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", "") + "\n \n" +
                "  &7Config Version: &a" + plugin.getConfig().getString("Version", "Unknown") + "\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n" +
                "  &7Latest Update: &9" + UpdateChecker.getLatestUpdateLink() + "\n" +
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯&r\n";
        if (sender instanceof Player) {
            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, message));
            return;
        }
        Bukkit.getServer().getConsoleSender().sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(message));
    }
}
