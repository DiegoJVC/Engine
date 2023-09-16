package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.grant.GrantAppliedEvent;
import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.checks.checks.AddGrantRedisCheck;
import com.cobelpvp.engine.util.Duration;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class SetCommand {

    @Command(names = {"rank set"}, permission = "engine.rank.set")
    public static void rankSetCommand(CommandSender sender, @Param(name = "player") UUID uuid, @Param(name = "displayName") String displayName, @Param(name = "duration") String time, @Param(name = "reason", wildcard = true) String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        Rank rank = Rank.getRankByDisplayName(displayName);

        Duration duration = Duration.fromString(time);

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ColorText.translate("&cCannot found this profile."));
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(ColorText.translate("&cThis value is invalid."));
            return;
        }

        OGrant grant = new OGrant(UUID.randomUUID(), rank, (sender instanceof Player ? ((Player) sender).getUniqueId() : null), System.currentTimeMillis(), reason, duration.getValue());

        profile.getGrants().add(grant);
        profile.save();
        profile.activateNextGrant();

        Engine.getInstance().getRedis().sendPacket(new AddGrantRedisCheck(profile.getId(), grant));

        assert rank != null;
        sender.sendMessage(ColorText.translate("&6Granted rank &e" + rank.getFormattedName() + " &6to &c" + TeamsUUIDCache.name(uuid)));

        Player player = profile.getPlayer();

        if (player != null) {
            new GrantAppliedEvent(player, grant).call();
        }
    }
}
