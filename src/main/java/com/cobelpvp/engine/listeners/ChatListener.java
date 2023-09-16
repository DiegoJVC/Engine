package com.cobelpvp.engine.listeners;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.util.Cooldowns;
import com.cobelpvp.atheneum.util.TimeUtils;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.chat.Chat;
import com.cobelpvp.engine.chat.ChatAttempt;
import com.cobelpvp.engine.chat.ChatAttemptEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Predicate;

public class ChatListener implements Listener {

    private Chat chat;

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        ChatAttempt chatAttempt = Engine.getInstance().getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

        Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

        Bukkit.getServer().getPluginManager().callEvent(chatAttemptEvent);

        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
                    event.setFormat((ColorText.translate(profile.getActiveGrant().getRank().getGamePrefix() + profile.getColoredUsername() + "&f: &f") + "%2$s"));
                    Cooldowns.addCooldown("globalChatCooldown", event.getPlayer(), TimeUtils.parseTime("5s"));
                }
                break;
                case MESSAGE_FILTERED: {
                    event.setCancelled(true);
                    chatAttempt.getFilterFlagged().punish(event.getPlayer());
                }
                break;
                case PLAYER_MUTED: {
                    event.setCancelled(true);

                    if (chatAttempt.getPunishment().isPermanent()) {
                        event.getPlayer().sendMessage(ChatColor.RED + "You are currently muted forever.");
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You are currently muted for another " + chatAttempt.getPunishment().getTimeRemaining() + ".");
                    }
                }
                break;
                case CHAT_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "Server chat is currently muted.");
                }
                break;
                case CHAT_DELAYED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ColorText.translate("&cYou cannot talk in " + Cooldowns.getCooldownForPlayerInt("globalChatCooldown", event.getPlayer()) + "&c."));
                }
                break;
            }
        }

        if (chatAttempt.getResponse() == ChatAttempt.Response.ALLOWED) {
            event.getRecipients().removeIf(new Predicate<Player>() {
                @Override
                public boolean test(Player player) {
                    Profile profile = Profile.getProfileMap().get(player.getUniqueId());
                    return profile != null && !profile.getOptions().isPublicChatEnabled();
                }
            });
        }
    }

}
