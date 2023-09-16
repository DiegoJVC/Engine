package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
public class StaffLeaveRedisCheck implements Packet {

    private String playerName;
    private String serverName;

    public StaffLeaveRedisCheck() {

    }

    public StaffLeaveRedisCheck(String playerName, String serverName) {
        this.playerName = playerName;
        this.serverName = serverName;
    }

    @Override
    public int id() {
        return 10;
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
