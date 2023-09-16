package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
public class StaffJoinRedisCheck implements Packet {

    private String playerName;
    private String serverName;

    public StaffJoinRedisCheck() {

    }

    public StaffJoinRedisCheck(String playerName, String serverName) {
        this.playerName = playerName;
        this.serverName = serverName;
    }

    @Override
    public int id() {
        return 9;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("playerName", playerName)
                .addProperty("serverName", serverName)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName = jsonObject.get("serverName").getAsString();
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerName() {
        return serverName;
    }
}
