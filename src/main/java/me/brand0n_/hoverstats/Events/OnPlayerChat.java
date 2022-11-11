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
            e.setFormat(formatChat(p));
        }

        // Format chat based on the current chat format
        String format = formatChat(e.getFormat(), p);
        String message = formatMessage(e.getMessage(), format);
        e.setMessage(message);

        // Send the player the hover able chat message
        sendHoverMessage(e.getRecipients(), format, e.getMessage(), p);

        // Make is so the second message is not sent
        e.getRecipients().clear();
    }

    private String formatChat(Player p) {
        String newFormatting = plugin.getConfig().getString("Chat Formatting.Format", "&7%displayname% &8&l> &7%message%")
                .replace("%message%", "%2$s")
                .replace("%name%", p.getName())
                .replace("%displayname%", "%1$s");
        return plugin.placeholders.addPlaceholders(p, newFormatting).replace("§", "&");
    }

    private String checkForEssentialsFormatting(String str) {
        StringBuilder newFormat = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            String character = String.valueOf(str.charAt(i));
            if (character.equalsIgnoreCase("{")) {
                if ((i + 1 < str.length())) {
                    String nextCharacter = String.valueOf(str.charAt(i + 1));
                    if (!nextCharacter.equalsIgnoreCase(" ") && !nextCharacter.equalsIgnoreCase("}")) {
                        newFormat.append("%");
                        continue;
                    }
                }
            }
            if (String.valueOf(str.charAt(i)).equalsIgnoreCase("}")) {
                if (!(i - 1 <= 0)) {
                    String lastCharacter = String.valueOf(str.charAt(i - 1));
                    if (!lastCharacter.equalsIgnoreCase(" ") && !lastCharacter.equalsIgnoreCase("{")) {
                        newFormat.append("%");
                        continue;
                    }
                }
            }
            newFormat.append(str.charAt(i));
        }
        return newFormat.toString();
    }

    private String formatChat(String str, Player p) {
        str = str.replace("%2$s", "");
        str = str.replace("%1$s", p.getDisplayName());
        hasFinalSpace = String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase(" ");
        str = str.trim();
        str = checkForEssentialsFormatting(str);
        str = plugin.colors.chatColor(str);
        return str.trim();
    }
    private String formatMessage(String str, String format) {
        str = plugin.colors.finalChatColor(format) + str; // Add chat color
        str = plugin.colors.chatColor(str);
        return str.replace("%2$s", str);
    }

    private TextComponent formatHoverMessage(String format, String message, Player p) {
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

    private void sendHoverMessage(Set<Player> recipients, String format, String message, Player p) {
        TextComponent hoverMessage = formatHoverMessage(format, message, p);

        for (Player player : recipients) {
            player.spigot().sendMessage(hoverMessage);
        }
    }
}
