package com.cobelpvp.engine.profile;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.util.GeoAPI;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.EngineConstants;
import com.cobelpvp.engine.moderator.qchecks.checks.StaffJoinRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.StaffLeaveRedisCheck;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ProfileListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getUniqueId());

        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "You already online, this is an error? contact with administrators.");
            return;
        }

        Profile profile = null;

        try {
            profile = new Profile(event.getName(), event.getUniqueId());

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("Failed to load your profile");
                return;
            }

            if (!Settings.bannedPlayersCanJoin) {
                if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                    handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
                    return;
                }
            }

            if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BAN_IP) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN_IP));
                return;
            }

            profile.setDisplayName(event.getName());

            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }

            profile.setLastServer(Bukkit.getServerName());
            profile.setLastSeen(System.currentTimeMillis());

            if (profile.getAddress() == null) {
                profile.setAddress(event.getAddress().getHostAddress());
            }

            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }

            profile.setAddress(event.getAddress().getHostAddress());

            for (Profile alt : Profile.getByIpAddress(event.getAddress().getHostAddress())) {
                profile.addAlt(alt);
                alt.addAlt(profile);
                if (profile != alt) {
                    alt.save();
                }

                if (!Settings.bannedPlayersCanJoin) {
                    if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
                        handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
                        return;
                    }
                }

                if (alt.getActivePunishmentByType(PunishmentType.BAN_IP) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN_IP));
                    return;
                }

                if (alt.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BLACKLIST));
                    return;
                }
            }

            profile.save();
        } catch (Exception e) {
            e.printStackTrace();
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }

        if (profile == null || !profile.isLoaded()) {
            event.setKickMessage(ChatColor.RED + "Failed to load your profile.");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        Profile.getProfileMap().put(profile.getUuid(), profile);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfileMap().get(player.getUniqueId());

        if (profile == null || !profile.isLoaded()) {
            player.kickPlayer(ChatColor.RED + "Failed to load your profile");
            System.out.println("Failed to load profile of " + profile.getDisplayName() + " kicked this.");
        }

        profile.setupBukkitPlayer(player);
        profile.setCountry(GeoAPI.getCountry(player.getAddress().getAddress()).getCountryName());

        if (player.hasPermission("moderator.staffjoin")) {
            Engine.getInstance().getRedis().sendPacket(new StaffJoinRedisCheck(profile.getColoredUsername(), Bukkit.getServerName()));
        }

        List<Profile> alts = Profile.getByIpAddress(profile.getAddress());

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Profile lastProfile = Profile.getByUsername(TeamsUUIDCache.name(event.getPlayer().getUniqueId()));

        assert lastProfile != null;
        lastProfile.save();

        if (event.getPlayer().hasPermission("moderator.staffleave")) {
            Engine.getInstance().getRedis().sendPacket(new StaffLeaveRedisCheck(lastProfile.getColoredUsername(), Bukkit.getServerName()));
        }

        Profile profile = Profile.getProfileMap().get(event.getPlayer().getUniqueId());

        if (profile.isLoaded()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    profile.save();
                }
            }.runTaskAsynchronously(Engine.getInstance());
        }
    }

    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(punishment.getKickMessage());
    }

    public static String getAltsMessage(List<Profile> alts) {
        List<String> messages = new ArrayList<>(alts.size());
        for (Profile altProfile : alts) {
            StringBuilder sb = new StringBuilder();
            if (altProfile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                sb.append(ChatColor.DARK_RED);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN_IP) != null) {
                sb.append(ChatColor.DARK_PURPLE);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                sb.append(ChatColor.RED);
            } else if (altProfile.getPlayer() == null) {
                sb.append(ChatColor.RED);
            } else {
                sb.append(ChatColor.GREEN);
            }
            sb.append(altProfile.getDisplayName());
            messages.add(sb.toString());
        }
        return StringUtils.join(messages, ", ");
    }
}
