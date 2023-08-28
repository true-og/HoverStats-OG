package me.brand0n_.hoverstats.Utils.Hover;


import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HoverUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static TextComponent setupHoverChatMessage(Player p, String message) {
       // Create the main hover component from provided message
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
        // Setup what will happen when the player clicks on the message
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Colors.chatColor(plugin.getConfig().getString("Hover.Stats Click Command", "/msg %player% ")
                .replace("%displayname%", p.getDisplayName()).replace("%player%", p.getName()))));

        // Get the defined hover message from the config
        List<String> path = plugin.getConfig().getStringList("Hover.Stats");
        // Check if the defined hover message is valid
        if (path.isEmpty()) {
            // Defined hover message is invalid load up the default messages
            path.add("&e&l%player%'s Stats:");
            path.add("");
            path.add("&e&lJoin Date:");
            path.add("#556DC8%first_joined%");
            path.add("");
            path.add("&e&lClick this to message player");
        }

        // Set the hover effect to be attached to the main messages
        mainComponent.setHoverEvent(formatHoverMessage(p, path));
        // Return the formatted text component
        return mainComponent;
    }

    public static TextComponent setupHoverJoinMessage(Player p, String message) {
        // Create the main hover component from provided message
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
        // Setup what will happen when the player clicks on the message
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Colors.chatColor(plugin.getConfig().getString("Hover.Join Click Command", "Welcome back")
                .replace("%displayname%", p.getDisplayName()).replace("%player%", p.getName()))));

        // Get the defined hover message from the config
        List<String> path = plugin.getConfig().getStringList("Hover.Join");
        // Check if the defined hover message is valid
        if (path.isEmpty()) {
            // Defined hover message is invalid load up the default messages
            path.add("&7Welcome back &b%displayname%&7!");
            path.add("");
            path.add("&e&lLast Online:");
            path.add("#556DC8%last_joined%");
            path.add("");
            path.add("&e&lClick this to say welcome back");
        }

        // Set the hover effect to be attached to the main messages
        mainComponent.setHoverEvent(formatHoverMessage(p, path));
        // Return the formatted text component
        return mainComponent;
    }

    public static TextComponent setupHoverFirstJoinMessage(Player p, String message) {
        // Create the main hover component from provided message
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
        // Setup what will happen when the player clicks on the message
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Colors.chatColor(plugin.getConfig().getString("Hover.First Join Click Command", "Welcome to the Server!")
                .replace("%displayname%", p.getDisplayName()).replace("%player%", p.getName()))));

        // Get the defined hover message from the config
        List<String> path = plugin.getConfig().getStringList("Hover.First Join");
        // Check if the defined hover message is valid
        if (path.isEmpty()) {
            // Defined hover message is invalid load up the default messages
            path.add("&7Welcome &b%displayname% &7to the server!");
            path.add("&7We hope you enjoy your stay!");
            path.add("");
            path.add("&e&lClick this to say \"Welcome to the Server!\"");
        }

        // Set the hover effect to be attached to the main messages
        mainComponent.setHoverEvent(formatHoverMessage(p, path));
        // Return the formatted text component
        return mainComponent;
    }

    public static TextComponent setupHoverLeaveMessage(Player p, String message) {
        // Create the main hover component from provided message
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
        // Setup what will happen when the player clicks on the message
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Colors.chatColor(plugin.getConfig().getString("Hover.Quit Click Command", "/mail %player% ")
                .replace("%displayname%", p.getDisplayName()).replace("%player%", p.getName()))));

        // Get the defined hover message from the config
        List<String> path = plugin.getConfig().getStringList("Hover.Quit");
        // Check if the defined hover message is valid
        if (path.isEmpty()) {
            // Defined hover message is invalid load up the default messages
            path.add("&e&l%player%'s Stats:");
            path.add("");
            path.add("&e&lJoin Date:");
            path.add("#556DC8%first_joined%");
            path.add("");
            path.add("&e&lClick this to message player");
        }

        // Set the hover effect to be attached to the main messages
        mainComponent.setHoverEvent(formatHoverMessage(p, path));
        // Return the formatted text component
        return mainComponent;
    }

    private static HoverEvent formatHoverMessage(Player p, List<String> strList) {
        // Declare the final output of the hover event, but make it empty
        HoverEvent finalOutput = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ArrayList<>());

        // Loop through all the elements in the string list provided
        for (int i = 0; i < strList.size(); i++) {
            // Format the output by first clearing any formatting from the previous message
            String output = ChatColor.RESET + strList.get(i);
            // Add in any placeholders that can be added to the message as well as formatting the colors
            output = Placeholders.addPlaceholders(p, output);

            // Create a new base component from the output text with all its formatting
            BaseComponent[] legacyOutput = TextComponent.fromLegacyText(output);
            // Loop through each layer of the newly created based component
            for (BaseComponent legacyComp : legacyOutput) {
                // Check if this part of the base component contains bold
                if (!legacyComp.toLegacyText().contains("§l")) {
                    // This line is not supposed to be bold, make sure no bold effect is added
                    legacyComp.setBold(false);
                }
            }

            // Add the formatted output to the final hover output
            finalOutput.addContent(new Text(legacyOutput));
            // Check if the current string index is not the final output
            if (i < strList.size() - 1) {
                // Since this isn't the final output add a line break to make each line give the illusion of separation
                finalOutput.addContent(new Text(Colors.chatColor("\n")));
            }
        }

        // Return the final hover message output
        return finalOutput;
    }
}
