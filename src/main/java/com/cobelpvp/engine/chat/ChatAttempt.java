package com.cobelpvp.engine.chat;

import com.cobelpvp.engine.punishment.Punishment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatAttempt {

    private Response response;
    private ChatFilter filterFlagged;
    private Punishment punishment;

    public ChatAttempt(Response response) {
        this.response = response;
    }

    public ChatAttempt(Response response, ChatFilter filterFlagged) {
        this.response = response;
        this.filterFlagged = filterFlagged;
    }

    public ChatAttempt(Response response, Punishment punishment) {
        this.response = response;
        this.punishment = punishment;
    }

    public enum Response {
        ALLOWED,
        MESSAGE_FILTERED,
        PLAYER_MUTED,
        CHAT_MUTED,
        CHAT_DELAYED;
    }

    public Response getResponse() {
        return response;
    }

    public ChatFilter getFilterFlagged() {
        return filterFlagged;
    }

    public Punishment getPunishment() {
        return punishment;
    }
}
