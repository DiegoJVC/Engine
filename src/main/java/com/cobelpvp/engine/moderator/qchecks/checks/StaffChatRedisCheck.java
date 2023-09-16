package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
@Setter
public class StaffChatRedisCheck implements Packet {

    private String playerName;
    private String serverName;
    private String chatMessage;

    public StaffChatRedisCheck() {

    }

    public StaffChatRedisCheck(String playerName, String serverName, String chatMessage) {
        this.playerName = playerName;
        this.serverName = serverName;
        this.chatMessage = chatMessage;
    }

    public int id() {
        return 8;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("playerName", playerName)
                .addProperty("serverName", serverName)
                .addProperty("chatMessage", chatMessage)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName = jsonObject.get("serverName").getAsString();
        chatMessage = jsonObject.get("chatMessage").getAsString();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getChatMessage() {
        return chatMessage;
    }
}