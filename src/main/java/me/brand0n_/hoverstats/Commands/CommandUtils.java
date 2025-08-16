package me.brand0n_.hoverstats.Commands;

import java.util.Objects;
import me.brand0n_.hoverstats.HoverStats;

public class CommandUtils {

    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void init() {

        setupMentionPlayerCommand();

    }

    private static void setupMentionPlayerCommand() {

        Objects.requireNonNull(plugin.getCommand("hoverstats")).setExecutor(new HoverStatsCommand());
        Objects.requireNonNull(plugin.getCommand("hoverstats")).setTabCompleter(new HoverStatsTabHandler());

    }

}
