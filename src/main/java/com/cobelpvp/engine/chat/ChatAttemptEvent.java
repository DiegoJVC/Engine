package com.cobelpvp.engine.chat;

import com.cobelpvp.engine.util.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class ChatAttemptEvent extends BaseEvent implements Cancellable {

    private final Player player;
    private final ChatAttempt chatAttempt;
    @Setter
    private String chatMessage;
    @Setter
    private boolean cancelled;
    @Setter
    private String cancelReason = "";

    public ChatAttemptEvent(Player player, ChatAttempt chatAttempt, String chatMessage) {
        this.player = player;
        this.chatAttempt = chatAttempt;
        this.chatMessage = chatMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public ChatAttempt getChatAttempt() {
        return chatAttempt;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
