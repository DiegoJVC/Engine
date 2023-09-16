package com.cobelpvp.engine.profile;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileConversations {

    @Getter
    private final Profile profile;
    @Getter
    private Map<UUID, Conversation> conversations;

    public ProfileConversations(Profile profile) {
        this.profile = profile;
        this.conversations = new HashMap<>();
    }

    public boolean canBeMessagedBy(Player player) {
        if (!profile.getOptions().isReceivingNewConversations()) {
            return conversations.containsKey(player.getUniqueId());
        }

        return true;
    }

    public Conversation getOrCreateConversation(Player target) {
        Player sender = profile.getPlayer();

        if (sender != null) {
            Conversation conversation = conversations.get(target.getUniqueId());

            if (conversation == null) {
                conversation = new Conversation(profile.getId(), target.getUniqueId());
            }

            return conversation;
        }

        return null;
    }

    public Conversation getLastRepliedConversation() {
        List<Conversation> list = conversations
                .values()
                .stream()
                .sorted(Comparator.comparingLong(Conversation::getLastMessageSentAt))
                .collect(Collectors.toList());

        Collections.reverse(list);

        return list.isEmpty() ? null : list.get(0);
    }

    public void expireAllConversations() {
        this.conversations.clear();
    }

    public Profile getProfile() {
        return profile;
    }

    public Map<UUID, Conversation> getConversations() {
        return conversations;
    }
}
