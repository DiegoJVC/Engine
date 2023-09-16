package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.util.Config;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.util.TaskUtil;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ImportCommand {

    @Command(names = {"rank import"}, permission = "engine.rank.import")
    public static void rankImportCommand(Player player) {
        if (Rank.getRanksMap().size() > 1) {
            player.sendMessage(ColorText.translate("&4Delete ranks before import ranks"));
            return;
        }

        Config config = new Config(Engine.getInstance(), "ranksForImport");
        TaskUtil.runTaskLater(() -> {
            int i = 0;
            for (String ranksString : config.getKeys(false)) {
                Rank rank = new Rank(UUID.randomUUID(), config.getString(ranksString + ".name"), config.getString(ranksString + ".prefix"), config.getString(ranksString + ".color"), config.getInt(ranksString + ".generalWeight"), config.getInt(ranksString + ".displayWeight"), config.getBoolean(ranksString + ".defaultRank"), config.getBoolean(ranksString + ".highRank"));

                for (String permission : config.getStringList(ranksString + ".permissions")) {
                    rank.getPermissions().add(permission);
                }

                rank.save();
                i++;
            }
            player.sendMessage(ColorText.translate("&6Imported " + i + " ranks..."));
        }, 40L);
    }
}
