package com.cobelpvp.engine.listeners;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BannedListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = Profile.getByUsername(event.getPlayer().getName());

        if (profile == null || !profile.isLoaded()) return;
        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);

        if (punishment != null) {
            event.getPlayer().sendMessage(ColorText.translate("&cYour account is currently banned, appeal this ban in " + Settings.discord + "."));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Profile profile = Profile.getByUsername(event.getPlayer().getName());

        if (profile == null || !profile.isLoaded()) return;
        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);

        if (punishment != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ColorText.translate("&cYour account is currently banned, appeal this ban in " + Settings.discord + "."));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Profile profile = Profile.getByUsername(event.getPlayer().getName());

        if (profile == null || !profile.isLoaded()) return;
        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);

        if (punishment != null) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            event.getPlayer().sendMessage(ColorText.translate("&cYour account is currently banned, appeal this ban in " + Settings.discord + "."));
        }
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUsername(player.getName());

        if (profile == null || !profile.isLoaded()) return;

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);

        if (punishment != null) {
            String command = event.getMessage().toLowerCase();

            if (command.startsWith("/msg")) return;
            if (command.startsWith("/r")) return;
            if (command.startsWith("/reply")) return;

            event.setCancelled(true);
            player.sendMessage(ColorText.translate("&cYour account is currently banned, appeal this ban in " + Settings.discord + "."));
            return;
        }
    }
}
