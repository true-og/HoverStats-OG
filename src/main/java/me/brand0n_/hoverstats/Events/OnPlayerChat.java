package me.brand0n_.hoverstats.Events;

import java.util.Set;
import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class OnPlayerChat implements EventExecutor, Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    private boolean hasFinalSpace = false;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.onPlayerChat((AsyncPlayerChatEvent) event);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Get the player
        Player p = e.getPlayer();

        // For plugin compatibility check if the event is cancelled by another plugin.
        if (e.isCancelled()) {
            return;
        }

        // Check if the plugin is using its own formatting
        if (plugin.getConfig().getBoolean("Chat Formatting.Use Formatting", true)) {
            // Get the format from config
            String newFormatting = getNewFormatting(
                    p, plugin.getConfig().getString("Chat Formatting.Format", "&7%displayname% &8&l> &7%message%"));
            // Set the chat format to be the new format defined in the config file
            e.setFormat(Placeholders.addPlaceholders(p, newFormatting).examinableName());
        }

        // Format chat based on the current chat format
        String format = formatChat(p, e.getFormat());
        // Format the message
        String message = formatMessage(p, e.getMessage(), format);
        // Set the message that the server will get equal to the newly formatted message
        e.setMessage(message);

        // Ensure that the event isn't cancelled
        if (!e.isCancelled()) {
            // Send the player the hover able chat message
            sendHoverMessage(p, e.getRecipients(), format, e.getMessage());

            // Make sure the player doesn't get the message twice
            e.getRecipients().clear();
        }
    }

    private String getNewFormatting(Player p, String format) {
        // Create an empty string builder element for future use
        StringBuilder newStr = new StringBuilder();
        // Add in the required placeholders to the provided format
        format = format
                // Replace the message placeholder with the default minecraft placeholder
                .replace("%message%", "%2$s")
                // Replace the name placeholder with the players name
                .replace("%name%", p.getName())
                // Replace the displayname placeholder with the default minecraft placeholder
                .replace("%displayname%", "%1$s");
        // Add in color formatting to the given format
        format = Placeholders.addPlaceholders(p, format).examinableName();

        // Loop through all the individual words in the provided format
        for (String formattedString : format.split(" ")) {
            // Check if the current string is either a space or is empty
            if (formattedString.equalsIgnoreCase("") || formattedString.equalsIgnoreCase(" ")) {
                // No further editing needed on this message, continue to the next message
                continue;
            }

            for (String tempString : formattedString.split("%")) {
                // Check if the current string that is being checked is empty
                if (tempString.equalsIgnoreCase("")) {
                    // No further editing needed on this message, continue to the next message
                    continue;
                }
                // Check if the current string that is being checked is a space
                if (tempString.equalsIgnoreCase(" ")) {
                    // Add message to the final message
                    newStr.append(tempString);
                    // Continue to the next message, nothing is left to do
                    continue;
                }
                // Check if the current string has a color code in it
                if (tempString.contains("§")) {
                    // Add message to the final message
                    newStr.append(tempString);
                    // Continue to the next message, nothing is left to do
                    continue;
                }

                // Check if the current string is equal to any vanilla formatting
                if (tempString.equalsIgnoreCase("1$s") || tempString.equalsIgnoreCase("2$s")) {
                    // Add message to the final message after adding a percent to the front
                    newStr.append("%").append(tempString);
                    // Continue to the next message, nothing is left to do
                    continue;
                }

                // Get the location where the string is used in the message
                int firstOccurrence = format.indexOf(tempString);

                // Check if the first occurrence of the word is at a valid location
                if (firstOccurrence > 0) {
                    // Get the character in front of the word
                    char previousCharacter = format.charAt(firstOccurrence - 1);
                    // Check if the character is a percent sign
                    if (previousCharacter == '%') {
                        // Add in any remaining strings
                        newStr.append("%%").append(tempString).append("%%");
                        // Continue to the next message, nothing is left to do
                        continue;
                    }
                }

                // Add in any remaining strings
                newStr.append(tempString);
            }
            // Add in an extra space
            newStr.append(" ");
        }
        // Add in any and all placeholders that are left in this message
        return newStr.toString().trim();
    }

    private String formatChat(Player p, String str) {
        // Replace the vanilla formatting with an empty space to make way for the plugin's custom formatting
        str = str.replace("%2$s", "");
        // Replace the vanilla formatting with the players display name
        str = str.replace("%1$s", p.getDisplayName());
        // Check if the string has a space before the message
        hasFinalSpace = String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase(" ");
        // Colorize the new formatting
        str = Colors.chatColor(str);
        // Return the updated format after removing any trailing or leading spaces to clean up the output
        return str.trim();
    }

    private String formatMessage(Player p, String str, String format) {
        // Add the final color from the format to the string
        str = Colors.finalChatColor(format) + str;

        // Check if the player is allowed to use hex colors in chat
        if (Permissions.hasPermission(p, "hoverstats.colors.hex")) {
            // Player has access to use hex colors in chat, colors any hex in the message
            str = Colors.addHex(str);
        }

        // Loop through all colors loaded by spigot
        for (ChatColor color : Colors.getAllRegisteredChatColors()) {
            // Check if the string contains a color code
            if (!str.contains("&" + color.getChar())) {
                // String doesn't contain color code, continue to next color
                continue;
            }
            // Check if the player has the permission to use this color
            if (Permissions.hasPermission(p, "hoverstats.colors", color.name().toLowerCase())
                    || Permissions.hasPermission(p, "hoverstats.colors.*")) {
                // Player has the permission to use this color code, replace the color code with the formatted color
                str = str.replace("&" + color.getChar(), color.toString());
            }
        }
        // Loop through all magic colors loaded
        for (ChatColor magic : Colors.getAllRegisteredMagicColors()) {
            // Check if the string contains a magic code
            if (!str.contains("&" + magic.getChar())) {
                // String doesn't contain magic code, continue to next color
                continue;
            }
            // Check if the player has the permission to use this color
            if (Permissions.hasPermission(p, "hoverstats.magic", magic.name().toLowerCase())
                    || Permissions.hasPermission(p, "hoverstats.magic.*")) {
                // Player has the permission to use this magic code, replace the magic code with the formatted color
                str = str.replace("&" + magic.getChar(), magic.toString());
            }
        }
        // Replace the string message with the provided string and return it
        return str.replace("%2$s", str);
    }

    private TextComponent formatHoverMessage(Player p, String format, String message) {
        // Make an empty main message element for future use
        TextComponent mainMessage = new TextComponent();
        // Create the hover event by calling on another method
        TextComponent hoverEvents = HoverUtils.setupHoverChatMessage(p, format);
        // Create a message element that will hold the formatted message
        TextComponent eMessage = new TextComponent(TextComponent.fromLegacyText(message.replace("%2$s", "")));

        // Check if the output had a final space in front of the message
        if (hasFinalSpace) {
            // There is supposed to be a space separating the message and the player name, replace the current message
            // with the newly formatted one with a space
            eMessage = new TextComponent(TextComponent.fromLegacyText(" " + message.replace("%2$s", "")));
        }

        // Add the hover message to the final output
        mainMessage.addExtra(hoverEvents);
        // Add the message to the final output
        mainMessage.addExtra(eMessage);
        // Return the final output
        return mainMessage;
    }

    private void sendHoverMessage(Player p, Set<Player> recipients, String format, String message) {
        // Get the final output message
        TextComponent hoverMessage = formatHoverMessage(p, format.replace("%%", "%"), message);

        // Loop through all online players
        for (Player player : recipients) {
            // Send each player the final outputted message
            player.spigot().sendMessage(hoverMessage);
        }
    }
}
