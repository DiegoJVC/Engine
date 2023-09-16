package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import com.cobelpvp.bond.packet.Packet;
import com.cobelpvp.engine.json.JsonChain;

public class VIPAnnouncementCheck implements Packet {

    private String playerName;
    private String serverName;

    public VIPAnnouncementCheck() {

    }

    public VIPAnnouncementCheck(String playerName, String serverName) {
        this.playerName = playerName;
        this.serverName = serverName;
    }

    @Override
    public int id() {
        return 97;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("playerName", playerName).addProperty("serverName", serverName).get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName  = jsonObject.get("serverName").getAsString();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerName() {
        return serverName;
    }
}
