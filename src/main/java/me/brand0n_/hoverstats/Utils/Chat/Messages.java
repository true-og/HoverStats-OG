package me.brand0n_.hoverstats.Utils.Chat;

import java.util.List;
import me.brand0n_.hoverstats.HoverStats;
import net.kyori.adventure.text.TextComponent;
import net.trueog.utilitiesog.UtilitiesOG;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void sendHelpMessage(CommandSender sender) {

        List<String> path = plugin.getConfig().getStringList("Messages.Help");
        for (String MESSAGE : path) {

            if (sender instanceof Player) {

                sender.sendMessage(Placeholders.addPlaceholders((Player) sender, MESSAGE));
                continue;

            }

            Bukkit.getServer().getConsoleSender()
                    .sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(MESSAGE));

        }

    }

    public static void reloaded(CommandSender sender) {

        String path = plugin.getConfig().getString("Messages.System Messages.Success.Reloaded",
                "%success% %pluginname%'s &7config has successfully reloaded!");
        if (sender instanceof Player) {

            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;

        }

        Bukkit.getServer().getConsoleSender().sendMessage(Placeholders.formatPlaceholders(path));

    }

    public static void noPermissions(CommandSender sender) {

        String path = plugin.getConfig().getString("Messages.System Messages.Error.No Permissions",
                "%error% &7You don't have permission to run this command!");
        if (sender instanceof Player) {

            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, path));
            return;

        }

        Bukkit.getServer().getConsoleSender()
                .sendMessage("[" + plugin.getName() + "]" + Placeholders.formatPlaceholders(path));

    }

    public static TextComponent papiHookSuccess() {

        String path = plugin.getConfig().getString("Messages.System Messages.Success.PAPI Hook",
                "&aFound PlaceholderAPI, hooking into it.");
        return UtilitiesOG.trueogColorize(Placeholders.formatPlaceholders(path));

    }

    public static TextComponent papiHookFailed() {

        String path = plugin.getConfig().getString("Messages.System Messages.Error.PAPI Hook",
                "&aCouldn't find PlaceholderAPI, all placeholders from PAPI won't work.");
        return UtilitiesOG.trueogColorize(Placeholders.formatPlaceholders(path));

    }

    public static void sendVersionInfo(CommandSender sender) {

        String message = "[" + plugin.getName() + "]" + "TrueOG Network 1.19.4 branch";
        if (sender instanceof Player) {

            sender.sendMessage(Placeholders.addPlaceholders((Player) sender, message));
            return;

        }

        Bukkit.getServer().getConsoleSender().sendMessage(message);

    }

}
