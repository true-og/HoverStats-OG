package me.brand0n_.hoverstats.Utils.Hover;


import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HoverUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public TextComponent setupHoverMessage(Player p, String message) {
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(plugin.colors.chatColor(message)));
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.colors.chatColor(plugin.variable.hoverCommand().replace("%player%", p.getName()))));
        mainComponent.setHoverEvent(formatHoverMessage(p, plugin.variable.statsHover()));
        return mainComponent;
    }

    private HoverEvent formatHoverMessage(Player p, List<String> strList) {
        HoverEvent finalOutput = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ArrayList<>());
        
        for (int i=0; i < strList.size(); i++) {
            String output = strList.get(i);
            output = output.replace("[", "").replace("]", "");
            output = plugin.placeholders.addPlaceholders(p, output);
            finalOutput.addContent(new Text(output));
            if (i < strList.size()-1) {
                finalOutput.addContent(new Text("\n"));
            }
        }

        return finalOutput;
    }
}
