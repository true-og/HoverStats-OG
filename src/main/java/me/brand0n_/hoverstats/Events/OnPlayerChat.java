package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class OnPlayerChat implements Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    private boolean hasFinalSpace = false;

    // Setting the Event Priority to Highest it makes it so the plugin has the final say in the chat event.
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        // For plugin compatibility check if the event is cancelled by another plugin.
        if (e.isCancelled()) {
            return;
        }

        // Check if the plugin is using its own formatting
        if (plugin.getConfig().getBoolean("Chat Formatting.Use Formatting", true)) {
            String newFormatting = getNewFormatting(p, plugin.getConfig().getString("Chat Formatting.Format", "&7%displayname% &8&l> &7%message%"));
            e.setFormat(plugin.placeholders.addPlaceholders(p, newFormatting));
        }

        // Format chat based on the current chat format
        String format = formatChat(p, e.getFormat());
        String message = formatMessage(e.getMessage(), format);
        e.setMessage(message);
        // Send the player the hover able chat message
        sendHoverMessage(p, e.getRecipients(), format, e.getMessage());

        // Make sure the player doesn't get the message twice
        e.getRecipients().clear();
    }

    private String getNewFormatting(Player p, String str) {
        StringBuilder stringBuffer = new StringBuilder();
        for (String formattedString : str.split(" ")) {
            if (formattedString.equalsIgnoreCase("%1$s") || formattedString.equalsIgnoreCase("%2$s")) {
                continue;
            }
            formattedString = formattedString.replaceFirst("%", "{")
                    .replace("%", "}");
            stringBuffer.append(formattedString);
            stringBuffer.append(" ");
        }
        return stringBuffer.toString().trim()
                .replace("{message}", "%2$s")
                .replace("{name}", p.getName())
                .replace("{displayname}", "%1$s");
    }

    private String formatChat(Player p, String str) {
        str = str.replace("%2$s", "");
        str = str.replace("%1$s", p.getDisplayName());
        hasFinalSpace = String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase(" ");
        str = str.trim();
        str = plugin.colors.chatColor(str);
        return str.trim();
    }

    private String formatMessage(String str, String format) {
        str = plugin.colors.finalChatColor(format) + str; // Add chat color
        str = plugin.colors.chatColor(str);
        return str.replace("%2$s", str);
    }

    private TextComponent formatHoverMessage(Player p, String format, String message) {
        TextComponent mainMessage = new TextComponent();
        TextComponent hoverEvents = plugin.hoverUtils.setupHoverMessage(p, plugin.colors.chatColor(format));
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
