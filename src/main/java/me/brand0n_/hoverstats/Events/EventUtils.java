package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void init() {
        plugin.getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, new OnPlayerChat(),
                // Get the event priority
                EventPriority.valueOf(plugin.getConfig().getString("Chat Event Priority", "HIGH")),
                new OnPlayerChat(), plugin);
        plugin.getServer().getPluginManager().registerEvent(PlayerJoinEvent.class, new OnPlayerJoin(),
                // Get the event priority
                EventPriority.valueOf(plugin.getConfig().getString("Join Event Priority", "HIGH")),
                new OnPlayerJoin(), plugin);
        plugin.getServer().getPluginManager().registerEvent(PlayerQuitEvent.class, new OnPlayerLeave(),
                // Get the event priority
                EventPriority.valueOf(plugin.getConfig().getString("Quit Event Priority", "HIGH")),
                new OnPlayerLeave(), plugin);
    }

    public static void unRegister() {
        // Unregister the player chat event
        HandlerList.unregisterAll(new OnPlayerChat());
        // Unregister the player join event
        HandlerList.unregisterAll(new OnPlayerJoin());
        // Unregister the player leave event
        HandlerList.unregisterAll(new OnPlayerLeave());
    }

}