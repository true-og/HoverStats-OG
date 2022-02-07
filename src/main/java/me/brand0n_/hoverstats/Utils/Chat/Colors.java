package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main
    // Setup Hex Pattern
    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    // Set chat color
    public String chatColor(String s) {
        // Check if the server is 1.16
        if (plugin.version.isCorrectVersionHex()) {
            // Check if there is a hex in the string
            Matcher match = pattern.matcher(s);
            while (match.find()) {
                // Get hex codes
                String color = s.substring(match.start(), match.end());
                // Convert to hex code
                s = s.replace(color, ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        // Allow user to use & instead of the weird minecraft color codes
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}