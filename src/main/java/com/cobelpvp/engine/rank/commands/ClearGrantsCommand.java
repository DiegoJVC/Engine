package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.checks.checks.ClearGrantsRedisCheck;
import org.bukkit.entity.Player;
import java.util.UUID;

public class ClearGrantsCommand {

    @Command(names = {"cleargrants"}, permission = "engine.rank.cleargrants")
    public static void clearGrantsCommand(Player player, @Param(name = "target") UUID target) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(target));

        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        profile.getGrants().clear();
        profile.save();

        player.sendMessage(ColorText.translate(profile.getDisplayName() + "&6:&e cleared grants"));

        Engine.getInstance().getRedis().sendPacket(new ClearGrantsRedisCheck(profile.getUuid()));
    }
}
