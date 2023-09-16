package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdRules {

    private static final List<String> RULES_MESSAGE = ImmutableList.of(
            "§6§l[CobelPvP Rules]",
            "§7-§cButterfly Clicking is not allowed",
            "§7-§cAny type of illegal modification of the minecraft client is not allowed",
            "§7-§cToxicity and Racism will end in a punishment",
            "§7-§cDisrespect to any member of the Staff is not allowed.",
            "§7-§cTrapping on less than 4x4 is completely prohibited on the server."
    );

    @Command(names = {"rules", "reglas"}, permission = "")
    public static void rules(Player sender) {
        RULES_MESSAGE.forEach(sender::sendMessage);
    }

}
