package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Placeholders {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public boolean hasPAPI() {
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return false;
        }
        plugin.variable.usePAPI = true;
        return true;
    }

    public String addPlaceholders(Player p, String str) {
        if (plugin.variable.usePAPI) {
            str = PlaceholderAPI.setPlaceholders(p, str);
        }
        if (p != null) {
            str = str.replace("%player%", p.getName());
        }
        return formatPlaceholders(str);
    }

    public String formatPlaceholders(String msg) {
        return plugin.colors.chatColor(msg
                .replace("%prefix%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Prefix")))
                .replace("%error%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Error")))
                .replace("%success%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Success")))
                .replace("%cmdname%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Command Name")))
                .replace("%pluginname%", Objects.requireNonNull(plugin.getConfig().getString("Placeholders.Plugin Name"))));
    }
}
