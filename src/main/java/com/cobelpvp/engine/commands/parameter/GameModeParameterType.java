package com.cobelpvp.engine.commands.parameter;

import com.cobelpvp.atheneum.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameModeParameterType implements ParameterType<GameMode> {

    private static final Map<String, GameMode> MAP = new HashMap<>();

    static {
        MAP.put("c", GameMode.CREATIVE);
        MAP.put("creative", GameMode.CREATIVE);
        MAP.put("1", GameMode.CREATIVE);

        MAP.put("s", GameMode.SURVIVAL);
        MAP.put("survival", GameMode.SURVIVAL);
        MAP.put("0", GameMode.SURVIVAL);

        MAP.put("a", GameMode.ADVENTURE);
        MAP.put("adventure", GameMode.ADVENTURE);
        MAP.put("2", GameMode.ADVENTURE);
    }

    @Override
    public GameMode transform(CommandSender commandSender, String s) {
        if (!MAP.containsKey(s.toLowerCase())) {
            commandSender.sendMessage(ChatColor.RED + s + " is not a valid game mode.");
            return (null);
        }

        return MAP.get(s.toLowerCase());
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        return (MAP.keySet().stream().filter(string -> StringUtils.startsWithIgnoreCase(string, s)).collect(Collectors.toList()));
    }
}