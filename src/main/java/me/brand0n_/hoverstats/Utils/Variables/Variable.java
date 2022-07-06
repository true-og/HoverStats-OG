package me.brand0n_.hoverstats.Utils.Variables;

import me.brand0n_.hoverstats.HoverStats;

import java.util.List;

public class Variable {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public boolean usePAPI = false;

    public List<String> statsHover() {
        List<String> path = plugin.getConfig().getStringList("Hover.Stats");
        if (path.isEmpty()) {
            path.add("&e&l%player%'s Stats:");
            path.add("");
            path.add("&3&lJoin Date:");
            path.add("%first_joined%");
            path.add("");
            return path;
        }
        return path;
    }

    public String hoverCommand() {
        return plugin.getConfig().getString("Hover.Stats Click Command");
    }

    public String placeholderPlaceholder() {
        return plugin.getConfig().getString("Placeholders.Placeholder Filler");
    }

    public boolean useChatFormatting = plugin.getConfig().getBoolean("Chat Formatting.Use Formatting");
    public String chatFormatting = plugin.getConfig().getString("Chat Formatting.Format");
}
