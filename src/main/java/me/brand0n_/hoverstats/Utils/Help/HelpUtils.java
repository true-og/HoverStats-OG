package me.brand0n_.hoverstats.Utils.Help;

import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public void formatHelpMessage(CommandSender sender, String configPath) {
        List<String> path = plugin.getConfig().getStringList(configPath);

        for (String messages : path) {
            sender.sendMessage(plugin.placeholders.formatPlaceholders(formatHelpLine(sender, messages)));
        }
    }

    private String formatHelpLine(CommandSender sender, String str) {
        return plugin.placeholders.addPlaceholders((Player) sender, str);
    }

    public void formatHelpMessage(String configPath) {
        List<String> path = plugin.getConfig().getStringList(configPath);

        for (String messages : path) {
            plugin.getLogger().info(plugin.placeholders.formatPlaceholders(messages));
        }
    }
}