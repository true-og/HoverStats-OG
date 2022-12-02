package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    // Setup Hex Pattern
    public static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    // Check if the plugin version is hex compatible
    public static boolean isCorrectVersionHex() {
        // Isolate the version value in Bukkit version
        String versionStr = Bukkit.getVersion().split("\\(MC:")[1].replace(")", "").strip();
        // Get just the version number, don't care about anything else
        String[] versionArry = versionStr.split("\\.");
        // Attempt to convert the version string to an integer
        try {
            // Convert version to an integer
            int version = Integer.parseInt(versionArry[1]);
            // Check if the integer is greater than 16
            return version >= 16;
        } catch (NumberFormatException e) {
            // Tell the user that their current version doesn't support hex
            Bukkit.getServer().getConsoleSender().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "[" + plugin.getName() + "]" + " &6WARNING &8| &7Your current server version (" + versionStr + ") doesn't support hex color codes."));
            Bukkit.getServer().getConsoleSender().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "[" + plugin.getName() + "] &eINFO &8| &7Hex color is only supported in 1.16 and above."));
            Bukkit.getServer().getConsoleSender().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "[" + plugin.getName() + "] &9Support &8| If you believe this is an issue please contact us on discord @ https://discord.gg/eYW6tTuCKz."));
            // If the string isn't a number return false
            return false;
        }
    }

    // Set chat color
    public static String chatColor(String s) {
        // Check if the server is 1.16
        if (plugin.useHex) {
            // Check if there is a hex in the string
            Matcher match = hexPattern.matcher(s);

            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                ChatColor hexColor = ChatColor.of(color);
                s = s.replace(color, hexColor + "");
                match = hexPattern.matcher(s);
            }
        }
        // Allow user to use & instead of the weird minecraft color codes
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Get final color
    public static String finalChatColor(String str) {
        return org.bukkit.ChatColor.getLastColors(str);
    }

}