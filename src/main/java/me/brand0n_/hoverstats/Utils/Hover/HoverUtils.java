package me.brand0n_.hoverstats.Utils.Hover;


import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.List;

public class HoverUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public TextComponent setupHoverMessage(Player p, String message) {
        String hover = plugin.colors.chatColor(formatHoverMessage(p, plugin.variable.statsHover()));
        Text hoverText = plugin.colors.textCompChatColor(hover);

        TextComponent mainComponent = new TextComponent("");

        mainComponent.addExtra(plugin.colors.chatColor(message));
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.colors.chatColor(plugin.variable.hoverCommand().replace("%player%", p.getName()))));
        mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        return mainComponent;
    }

    private String formatHoverMessage(Player p, List<String> strList) {
        String str = strList.toString();
        str = str.trim();
        str = str.substring(1, str.length() - 1);
        str = str.replaceAll(", ", "\n&f");
        str = plugin.placeholders.addPlaceholders(p, str);
        if (str.contains("{") && str.contains("}") && str.length() > 2) {
            str = str.replace("{", "%").replace("}", "%");
        }
        if (str.equalsIgnoreCase("null") || str.isEmpty()) {
            return plugin.variable.placeholderPlaceholder();
        }
        return str;
    }
}
