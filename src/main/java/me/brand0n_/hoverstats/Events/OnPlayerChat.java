package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    // Setting the Event Priority to Highest it makes it so the plugin has the final say in the chat event.
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // For plugin compatibility check if the event is cancelled by another plugin.
        if (e.isCancelled()) {
            return;
        }

        String message = e.getMessage();
        String format = e.getFormat().replace("%2$s", "");

        format = format.replace("%1$s", e.getPlayer().getDisplayName());
        boolean hasFinalSpace = String.valueOf(format.charAt(format.length() - 1)).equalsIgnoreCase(" ");

        format = format.trim();
        format = checkForEssentialsFormatting(format);
        format = plugin.colors.chatColor(format);
        format = format.trim();

        message = plugin.colors.finalChatColor(format) + message; // Add chat color
        message = plugin.colors.chatColor(message);


        e.setFormat(plugin.colors.chatColor(checkForEssentialsFormatting(e.getFormat().replace("%1$s", e.getPlayer().getDisplayName()))));
        e.setMessage(message.replace("%2$s", message));

        TextComponent mainMessage = new TextComponent();
        TextComponent hoverEvents = plugin.hoverUtils.setupHoverMessage(e.getPlayer(), plugin.colors.chatColor(format));
        TextComponent eMessage;

        if (hasFinalSpace) {
            eMessage = new TextComponent(TextComponent.fromLegacyText(" " + e.getMessage()));
        } else {
            eMessage = new TextComponent(TextComponent.fromLegacyText(e.getMessage()));
        }

        mainMessage.addExtra(hoverEvents);
        mainMessage.addExtra(eMessage);

        for (Player p : e.getRecipients()) {
            p.spigot().sendMessage(mainMessage);
        }

        // Make is so the second message is not sent
        e.getRecipients().clear();
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
}
