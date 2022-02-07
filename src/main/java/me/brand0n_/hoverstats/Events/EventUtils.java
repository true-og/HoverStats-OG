package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;

public class EventUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void init() {
        setupOnPlayerChat();

    }

    private static void setupOnPlayerChat() {
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerChat(), plugin);
    }
}