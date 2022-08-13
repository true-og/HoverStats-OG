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

        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(plugin.colors.chatColor(message)));

        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.colors.chatColor(plugin.variable.hoverCommand().replace("%player%", p.getName()))));
        mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        return mainComponent;
    }

    private String formatHoverMessage(Player p, List<String> strList) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < strList.size(); i++) {
            String str = strList.get(i);
            str = plugin.placeholders.addPlaceholders(p, str);
            if (str.contains("{") && str.contains("}") && str.length() > 2) {
                str = str.replace("{", "%").replace("}", "%");
            }
            if (i < strList.size()-1) {
                output.append(str.trim()).append("\n&r&f");
                continue;
            }
            output.append(str.trim()).append("&r&f");
        }
        if (output.toString().equalsIgnoreCase("null") || output.toString().isEmpty()) {
            output.append(plugin.variable.placeholderPlaceholder());
        }
        return plugin.colors.chatColor(output.toString());
    }
}
