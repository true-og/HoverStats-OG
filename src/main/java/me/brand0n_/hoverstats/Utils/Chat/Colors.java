package me.brand0n_.hoverstats.Utils.Chat;

import me.brand0n_.hoverstats.HoverStats;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main
    // Setup Hex Pattern
    private final Pattern pattern = Pattern.compile("#[A-Fa-f0-9]{6}");

    // Set chat color
    public String chatColor(String s) {
        // Check if the server is 1.16
        if (plugin.version.isCorrectVersionHex()) {
            // Check if there is a hex in the string
            Matcher match = pattern.matcher(s);

            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                ChatColor hexColor = ChatColor.of(color);
                s = s.replace(color, hexColor+"");
                match = pattern.matcher(s);
            }
        }
        // Allow user to use & instead of the weird minecraft color codes
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    // Text component chat color
    public Text textCompChatColor(String str) {
        Text textComp = new Text(chatColor(str));

        // Check if the server is 1.16
        if (plugin.version.isCorrectVersionHex()) {
            // Check if there is a hex in the string
            Matcher match = pattern.matcher(str);

            while (match.find()) {
                String color = str.substring(match.start(), match.end());
                ChatColor hexColor = ChatColor.of(color);
                str = str.replace(color, hexColor+"");
                textComp = new Text(str);
                match = pattern.matcher(str);
            }
        }
        // Allow user to use & instead of the weird minecraft color codes
        return textComp;
    }
}