package me.brand0n_.hoverstats.Utils.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.ChatColor;
import net.trueog.utilitiesog.UtilitiesOG;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Colors {

    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    // Check if the plugin version is hex compatible
    public static boolean isCorrectVersionHex() {
        // Isolate the version value in Bukkit version
        String versionStr =
                Bukkit.getVersion().split("\\(MC:")[1].replace(")", "").strip();
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
            UtilitiesOG.logToConsole(
                    HoverStats.getPrefix(),
                    "[" + plugin.getName() + "]" + " &6WARNING &8| &7Your current server version (" + versionStr
                            + ") doesn't support hex color codes.");
            UtilitiesOG.logToConsole(
                    HoverStats.getPrefix(),
                    "[" + plugin.getName() + "] &eINFO &8| &7Hex color is only supported in 1.16 and above.");
            // If the string isn't a number, return false.
            return false;
        }
    }

    public static List<Matcher> addRegex(String str) {
        // Create an empty array
        List<Matcher> regexList = new ArrayList<>();
        // Get the hex pattern configuration section
        ConfigurationSection hexPatterns = plugin.getConfig().getConfigurationSection("Hex Patterns");

        // Check if the hex patterns section exists
        if (hexPatterns == null) {
            // The hex pattern doesn't exist, send an empty array
            return regexList;
        }
        // Save the hex patterns to a string list
        Set<String> hexPatternStrings = hexPatterns.getKeys(false);

        // Loop through all the defined hex patterns
        for (String pattern : hexPatternStrings) {
            // Get the prefix for the pattern
            String prefix = plugin.getConfig().getString("Hex Patterns." + pattern + ".Prefix");
            // Get the suffix for the pattern
            String suffix = plugin.getConfig().getString("Hex Patterns." + pattern + ".Suffix");

            // Combine the prefix and suffix to make a regex expression
            Pattern regexExpression = Pattern.compile((prefix + "[a-fA-F0-9]{6}" + suffix).trim());

            // Add the newly created pattern to the array
            regexList.add(regexExpression.matcher(str));
        }

        // Add in the standard hex format (ensuring that it is the last pattern in the list)
        regexList.add(Pattern.compile("#[a-fA-F0-9]{6}").matcher(str));

        // Return whatever is in the regex list
        return regexList;
    }

    public static String addHex(String str) {
        // Check if the server is 1.16
        if (!plugin.useHex) {
            // Plugin doesn't use hex, return the unedited string
            return str;
        }
        // Set up a list of matches
        List<Matcher> matches = addRegex(str);

        // Loop through all loaded matches
        for (Matcher match : matches) {
            while (match.find()) {
                // Loop through all the groups
                for (int i = 0; i <= match.groupCount(); i++) {
                    // Get the string of the match
                    String matchStr = match.group(i);
                    // Loop through the match string checking if the first character is a hashtag ('#')
                    while (matchStr.charAt(0) != '#') {
                        // Remove whatever is in front of the '#'
                        matchStr = matchStr.substring(1);
                    }
                    // Check if the hex code is greater than the allowed amount of 7 characters
                    if (matchStr.length() > 7) {
                        // Remove everything after the matcher code
                        matchStr = matchStr.substring(0, 7);
                    }

                    // Replace the hex color with proper color formatting
                    str = str.replace(
                            match.group(i),
                            String.valueOf(ChatColor.of(matchStr)).strip());
                }
            }
        }

        // Return the formatted string
        return str;
    }

    // Set chat color
    public static String chatColor(String s) {
        // Attempt to add in hex colors
        s = addHex(s);
        // Allow user to use & instead of the weird minecraft color codes
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Get all chat colors
    public static List<org.bukkit.ChatColor> getAllRegisteredChatColors() {
        // Create a list of all chat colors that are registered
        List<org.bukkit.ChatColor> chatColors = new ArrayList<>();

        for (org.bukkit.ChatColor chatColor : org.bukkit.ChatColor.values()) {
            // Check if the chat color is equal to any of the magic values
            if (chatColor.equals(org.bukkit.ChatColor.MAGIC)
                    || chatColor.equals(org.bukkit.ChatColor.ITALIC)
                    || chatColor.equals(org.bukkit.ChatColor.BOLD)
                    || chatColor.equals(org.bukkit.ChatColor.STRIKETHROUGH)
                    || chatColor.equals(org.bukkit.ChatColor.UNDERLINE)) {
                continue;
            }
            // Add the chat colors to the list
            chatColors.add(chatColor);
        }

        // Return all chat colors
        return chatColors;
    }

    // Get all magic chat colors
    public static List<org.bukkit.ChatColor> getAllRegisteredMagicColors() {
        // Create a list of all magic colors
        List<org.bukkit.ChatColor> magicColors = new ArrayList<>();

        // Load Italics into the magic list
        magicColors.add(org.bukkit.ChatColor.ITALIC);
        // Load Magic into the magic list
        magicColors.add(org.bukkit.ChatColor.MAGIC);
        // Load Bold into the magic list
        magicColors.add(org.bukkit.ChatColor.BOLD);
        // Load Strikethrough into the magic list
        magicColors.add(org.bukkit.ChatColor.STRIKETHROUGH);
        // Load Underline into the magic list
        magicColors.add(org.bukkit.ChatColor.UNDERLINE);

        // Return all magic colors
        return magicColors;
    }

    // Get final color
    public static String finalChatColor(String str) {
        return org.bukkit.ChatColor.getLastColors(str);
    }
}
