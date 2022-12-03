package me.brand0n_.hoverstats.Utils.Hover;


import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HoverUtils {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static TextComponent setupHoverMessage(Player p, String message) {
        TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
        mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Colors.chatColor(plugin.getConfig().getString("Hover.Stats Click Command", "/msg %player% ").replace("%player%", p.getName()))));

        List<String> path = plugin.getConfig().getStringList("Hover.Stats");
        if (path.isEmpty()) {
            path.add("&e&l%player%'s Stats:");
            path.add("");
            path.add("&e&lJoin Date:");
            path.add("#556DC8%first_joined%");
            path.add("");
            path.add("&e&lClick this to message player");
        }

        mainComponent.setHoverEvent(formatHoverMessage(p, path));
        return mainComponent;
    }

    private static HoverEvent formatHoverMessage(Player p, List<String> strList) {
        HoverEvent finalOutput = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ArrayList<>());
        
        for (int i = 0; i < strList.size(); i++) {
            String output = ChatColor.RESET + strList.get(i);
            output = output.replace("[", "").replace("]", "");
            output = Placeholders.addPlaceholders(p, output);

            BaseComponent[] legacyOutput = TextComponent.fromLegacyText(output);
            for (BaseComponent legacyComp : legacyOutput) {
                if (!legacyComp.toLegacyText().contains("§l")) {
                    legacyComp.setBold(false);
                }
            }

            finalOutput.addContent(new Text(legacyOutput));
            if (i < strList.size() - 1) {
                finalOutput.addContent(new Text(Colors.chatColor("\n")));
            }
        }

        return finalOutput;
    }
}
