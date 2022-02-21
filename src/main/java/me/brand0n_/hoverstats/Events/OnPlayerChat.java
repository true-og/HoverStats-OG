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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        String format = e.getFormat().replace(" %2$s", " ");
        format = checkForEssentialsFormatting(format);

        TextComponent mainMessage = new TextComponent();
        TextComponent hoverEvents = plugin.hoverUtils.setupHoverMessage(e.getPlayer(), format);
        TextComponent eMessage = new TextComponent(e.getMessage());

        mainMessage.addExtra(hoverEvents);
        mainMessage.addExtra(eMessage);

        for (Player p : e.getRecipients()) {
            p.spigot().sendMessage(mainMessage);
        }
        e.setCancelled(true);
    }

    private String checkForEssentialsFormatting(String str) {
        StringBuilder newFormat = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            String character = String.valueOf(str.charAt(i));
            if (character.equalsIgnoreCase("{")) {
                if ((i + 1 < str.length())) {
                    String nextCharacter = String.valueOf(str.charAt(i+1));
                    if (!nextCharacter.equalsIgnoreCase(" ") && !nextCharacter.equalsIgnoreCase("}")) {
                        newFormat.append("%");
                        continue;
                    }
                }
            }
            if (String.valueOf(str.charAt(i)).equalsIgnoreCase("}")) {
                if (!(i - 1 <= 0)) {
                    String lastCharacter = String.valueOf(str.charAt(i-1));
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
