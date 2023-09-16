package com.cobelpvp.engine.chat;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.PunishmentType;
import lombok.Getter;
import com.cobelpvp.atheneum.util.Cooldowns;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    @Getter
    private boolean publicChatMuted = false;
    @Getter
    private final List<ChatFilter> filters = new ArrayList<>();
    @Getter
    private List<String> filteredPhrases = new ArrayList<>();
    @Getter
    private List<String> linkWhitelist = new ArrayList<>();

    public void togglePublicChatMute() {
        publicChatMuted = !publicChatMuted;
    }

    public ChatAttempt attemptChatMessage(Player player, String message) {
        Profile profile = Profile.getProfileMap().get(player.getUniqueId());

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
            return new ChatAttempt(ChatAttempt.Response.PLAYER_MUTED, profile.getActivePunishmentByType(PunishmentType.MUTE));
        }

        if (publicChatMuted && !player.hasPermission("engine.chatmute.bypass")) {
            return new ChatAttempt(ChatAttempt.Response.CHAT_MUTED);
        }

        if (Cooldowns.isOnCooldown("GlobalChatCooldown", player) && !player.hasPermission("engine.chatdelay.bypass")) {
            return new ChatAttempt(ChatAttempt.Response.CHAT_DELAYED);
        }

        String msg = message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "").trim();

        String[] words = msg.trim().split(" ");

        for (ChatFilter chatFilter : filters) {
            if (chatFilter.isFiltered(msg, words)) {
                return new ChatAttempt(ChatAttempt.Response.MESSAGE_FILTERED);
            }
        }

        return new ChatAttempt(ChatAttempt.Response.ALLOWED);
    }

    public boolean isPublicChatMuted() {
        return publicChatMuted;
    }

    public List<ChatFilter> getFilters() {
        return filters;
    }

    public List<String> getFilteredPhrases() {
        return filteredPhrases;
    }

    public List<String> getLinkWhitelist() {
        return linkWhitelist;
    }
}
