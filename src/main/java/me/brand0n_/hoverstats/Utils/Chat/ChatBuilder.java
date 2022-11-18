package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatBuilder {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public void sendHelpMessage(CommandSender sender, String configPath) {
        plugin.helpUtils.formatHelpMessage(sender, configPath);
    }

    public void sendHelpMessageConsole(String configPath) {
        plugin.helpUtils.formatHelpMessage(configPath);
    }

    public String reloaded(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.Reloaded");
        if (path == null) {
            String defaultPath = "%success% %pluginname%'s &7config has successfully reloaded!";
            return plugin.placeholders.formatPlaceholders(defaultPath);
        }
        return plugin.placeholders.addPlaceholders((Player) sender, path);
    }

    public String noPermissions(CommandSender sender) {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.No Permissions");
        if (path == null) {
            String defaultPath = "%error% &7You don't have permission to run this command!";
            return plugin.placeholders.formatPlaceholders(defaultPath);
        }
        return plugin.placeholders.addPlaceholders((Player) sender, path);
    }

    public String papiHookSuccess() {
        String path = plugin.getConfig().getString("Messages.System Messages.Success.PAPI Hook");
        if (path == null) {
            String defaultPath = "&aFound PlaceholderAPI, hooking into it.";
            return plugin.placeholders.formatPlaceholders(defaultPath);
        }
        return plugin.placeholders.formatPlaceholders(path);
    }
    public String papiHookFailed() {
        String path = plugin.getConfig().getString("Messages.System Messages.Error.PAPI Hook");
        if (path == null) {
            String defaultPath = "&aCouldn't find PlaceholderAPI, all placeholders from PAPI won't work.";
            return plugin.placeholders.formatPlaceholders(defaultPath);
        }
        return plugin.placeholders.formatPlaceholders(path);
    }
}
