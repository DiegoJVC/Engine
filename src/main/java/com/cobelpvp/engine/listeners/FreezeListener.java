package com.cobelpvp.engine.listeners;

import com.cobelpvp.engine.checks.checks.JoinFreezeRedisCheck;
import com.cobelpvp.engine.checks.checks.QuitFreezeRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FreezeListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (modSuiteProfile != null) {
            if (modSuiteProfile.isFreeze()) {
                double xTo = event.getTo().getX();
                double xFrom = event.getFrom().getX();
                double yTo = event.getTo().getY();
                double yFrom = event.getFrom().getY();
                double zTo = event.getTo().getZ();
                double zFrom = event.getFrom().getZ();

                if (event.getTo().locToBlock(xTo) != event.getFrom().locToBlock(xFrom) || event.getTo().locToBlock(zTo) != event.getFrom().locToBlock(zFrom) || event.getTo().locToBlock(yTo) != event.getFrom().locToBlock(yFrom)) {
                    player.teleport(event.getFrom());
                    player.sendMessage(ColorText.translate("&4&lWARNING! &cYou cannot move while frozen."));
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (event.getCause() == null) {
            return;
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (modSuiteProfile != null) {
                if (modSuiteProfile.isFreeze()) {
                    player.sendMessage(ColorText.translate("&4&lWARNING! &cYou cannot pearl while frozen."));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

            if (modSuiteProfile != null) {
                if (modSuiteProfile.isFreeze()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

            if (modSuiteProfile != null) {
                if (modSuiteProfile.isFreeze()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        Profile profile = Profile.getByUsername(player.getName());

        if (modSuiteProfile != null) {
            if (modSuiteProfile.isFreeze()) {
                if (profile == null || !profile.isLoaded()) return;
                Engine.getInstance().getRedis().sendPacket(new JoinFreezeRedisCheck(profile.getColoredUsername(), Bukkit.getServerName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (modSuiteProfile != null) {
            if (modSuiteProfile.isFreeze()) {
                Profile profile = Profile.getByUsername(player.getName());

                if (profile == null || !profile.isLoaded()) return;
                Engine.getInstance().getRedis().sendPacket(new QuitFreezeRedisCheck(profile.getActiveGrant().getRank().getGameColor() + profile.getDisplayName(), Bukkit.getServerName()));
            }
        }
    }
}
