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
        String format = e.getFormat().replace("%2$s", "");
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
}
