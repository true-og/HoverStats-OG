package me.brand0n_.hoverstats.Utils.Chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import me.brand0n_.hoverstats.HoverStats;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.TextComponent;
import net.trueog.utilitiesog.UtilitiesOG;
import org.bukkit.entity.Player;

public class Placeholders {

    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static boolean hasPAPI() {

        // Check if the plugin should try to hook into PlaceholderAPI
        if (plugin.usePAPI) {

            // Check if the PlaceholderAPI is installed on the server, return the result
            return plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        }

        // PlaceholderAPI isn't installed return false
        return false;

    }

    public static TextComponent addPlaceholders(Player p, String str) {

        // Check if the player is valid
        if (p != null) {

            // Get the time that the player first joined the server
            Date firstJoinDate = new Date(p.getFirstPlayed());
            // Get the last time that the player first joined the server
            Date lastJoinDate = new Date(p.getLastSeen());
            // Format the date to be in a readable format
            SimpleDateFormat sdf = new SimpleDateFormat(
                    plugin.getConfig().getString("Date Format", "MM/dd/yy hh:mm:ss"));

            // Format the string with all plugin specific placeholders
            str = str
                    // Replace the player with the players name
                    .replace("%player%", p.getName())
                    // Replace the first join time with the formatted first join time
                    .replace("%first_joined%", sdf.format(firstJoinDate))
                    // Replace the first join time with the formatted first join time
                    .replace("%last_joined%", sdf.format(lastJoinDate))
                    // Replace the display name with the players display name
                    .replace("%displayname%", p.displayName().examinableName())
                    // Replace the players username with the players name
                    .replace("%username%", p.getName())
                    // Replace the players username with the players name
                    .replace("%name%", p.getName())
                    // Replace the world with the current world the player is in
                    .replace("%world%", p.getWorld().getName());

        }

        // Check if the string has any brackets in it
        if (str.contains("{") && str.contains("}")) {

            // Remove any bracket placeholders with their proper formats
            str = PlaceholderAPI.setBracketPlaceholders(p, str);

        }

        // Check if the plugin has PlaceholderAPI installed
        if (plugin.usePAPI) {

            // PlaceholderAPI is allowed, replace any and all PlaceholderAPI placeholders
            // with their outputs
            str = PlaceholderAPI.setPlaceholders(p, str);

        }

        // Return the formatted result of the current string plugged into the rest of
        // the plugin specific placeholders
        return UtilitiesOG.trueogColorize(formatPlaceholders(str));

    }

    public static String formatPlaceholders(String msg) {

        // Make the color codes in the string result in colors
        return Colors.chatColor(msg
                // Replace the prefix with the prefix defined in the config
                .replace("%prefix%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Prefix")))
                // Replace the error prefix with the error prefix defined in the config
                .replace("%error%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Error")))
                // Replace the success prefix with the success prefix defined in the config
                .replace("%success%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Success")))
                // Replace the command name with the command name defined in the config
                .replace("%cmdname%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Command Name")))
                // Replace the command name with the command name defined in the config
                // (alternative formatting)
                .replace("%cmdName%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Command Name")))
                // Replace the plugin name with the plugin name defined in the config
                .replace("%pluginname%",
                        Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Plugin Name")))
                // Replace the plugin name with the plugin name defined in the config
                // (alternative formatting)
                .replace("%pluginName%",
                        Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Plugin Name"))));

    }

}
