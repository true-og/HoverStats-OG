package me.brand0n_.hoverstats.Events;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.brand0n_.hoverstats.HoverStats;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class EventUtils {

    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void init() {

        plugin.getServer().getPluginManager().registerEvent(AsyncChatEvent.class, new OnPlayerChat(),
                // Get the event priority.
                EventPriority.valueOf(plugin.getConfig().getString("Chat Event Priority", "HIGH")), new OnPlayerChat(),
                plugin);

    }

    public static void unRegister() {

        // Unregister the player chat event.
        HandlerList.unregisterAll(new OnPlayerChat());

    }

}
