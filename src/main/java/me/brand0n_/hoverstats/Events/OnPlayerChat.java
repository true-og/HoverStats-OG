package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.regex.Matcher;

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
            String newFormatting = getNewFormatting(p, plugin.getConfig().getString("Chat Formatting.Format", "&7%displayname% &8&l> &7%message%"));
            e.setFormat(Placeholders.addBracketPlaceholders(p, newFormatting));
        }

        // Format chat based on the current chat format
        String format = formatChat(p, e.getFormat()).replace("{", "%").replace("}", "%");
        String message = formatMessage(e.getMessage(), format);
        e.setMessage(message);
        // Send the player the hover able chat message
        sendHoverMessage(p, e.getRecipients(), format, e.getMessage());

        // Make sure the player doesn't get the message twice
        e.getRecipients().clear();
    }

    private String getNewFormatting(Player p, String str) {
        StringBuilder newStr = new StringBuilder();
        str = str
                .replace("%message%", "%2$s")
                .replace("%name%", p.getName())
                .replace("%displayname%", "%1$s");

        str = Colors.chatColor(str);

        for (String formattedString : str.split(" ")) {
            if (formattedString.equalsIgnoreCase("") || formattedString.equalsIgnoreCase(" ")) {
                continue;
            }

            if (formattedString.contains("{") && formattedString.contains("}")) {
                formattedString = formattedString.replace("{", "%").replace("}", "%");
            }
            for (String tempString : formattedString.split("%")) {
                if (tempString.equalsIgnoreCase("")) {
                    continue;
                }
                if (tempString.equalsIgnoreCase(" ")) {
                    newStr.append(tempString);
                    continue;
                }
                Matcher match = Colors.hexPattern.matcher(tempString);
                if (match.find()) {
                    newStr.append(tempString);
                    continue;
                }
                if (tempString.contains("§")) {
                    // Is color code
                    newStr.append(tempString);
                    continue;
                }

                if (tempString.equalsIgnoreCase("1$s") || tempString.equalsIgnoreCase("2$s")) {
                    newStr.append("%").append(tempString);
                    continue;
                }
                newStr.append("{").append(tempString).append("}");
            }
            newStr.append(" ");
        }
        return Placeholders.addPlaceholders(p, newStr.toString().trim());
    }

    private String formatChat(Player p, String str) {
        str = str.replace("%2$s", "");
        str = str.replace("%1$s", p.getDisplayName());
        hasFinalSpace = String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase(" ");
        str = str.trim();
        str = Colors.chatColor(str);
        return str.trim();
    }

    private String formatMessage(String str, String format) {
        str = Colors.finalChatColor(format) + str; // Add chat color
        str = Colors.chatColor(str);
        return str.replace("%2$s", str);
    }

    private TextComponent formatHoverMessage(Player p, String format, String message) {
        TextComponent mainMessage = new TextComponent();
        TextComponent hoverEvents = HoverUtils.setupHoverMessage(p, Colors.chatColor(format));
        TextComponent eMessage;

        if (hasFinalSpace) {
            eMessage = new TextComponent(TextComponent.fromLegacyText(" " + message.replace("%2$s", "")));
        } else {
            eMessage = new TextComponent(TextComponent.fromLegacyText(message.replace("%2$s", "")));
        }

        mainMessage.addExtra(hoverEvents);
        mainMessage.addExtra(eMessage);
        return mainMessage;
    }

    private void sendHoverMessage(Player p, Set<Player> recipients, String format, String message) {
        TextComponent hoverMessage = formatHoverMessage(p, format, message);

        for (Player player : recipients) {
            player.spigot().sendMessage(hoverMessage);
        }
    }
}
