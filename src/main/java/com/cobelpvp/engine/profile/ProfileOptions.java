package com.cobelpvp.engine.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class ProfileOptions {

    @Getter
    @Setter
    private boolean publicChatEnabled = true;
    @Getter
    @Setter
    private boolean receivingNewConversations = true;
    @Getter
    @Setter
    private boolean playingMessageSounds = true;

    public boolean isPublicChatEnabled() {
        return publicChatEnabled;
    }

    public boolean isReceivingNewConversations() {
        return receivingNewConversations;
    }

    public boolean isPlayingMessageSounds() {
        return playingMessageSounds;
    }

    public void setPublicChatEnabled(boolean publicChatEnabled) {
        this.publicChatEnabled = publicChatEnabled;
    }

    public void setReceivingNewConversations(boolean receivingNewConversations) {
        this.receivingNewConversations = receivingNewConversations;
    }

    public void setPlayingMessageSounds(boolean playingMessageSounds) {
        this.playingMessageSounds = playingMessageSounds;
    }
}
