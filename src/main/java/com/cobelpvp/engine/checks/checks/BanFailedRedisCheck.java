package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
@Setter
public class BanFailedRedisCheck implements Packet {

    private String serverName;
    private String playerName;
    private String targetName;

    public BanFailedRedisCheck() {

    }

    public BanFailedRedisCheck(String serverName, String playerName, String targetName) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.targetName = targetName;
    }

    @Override
    public int id() {
        return 26;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("targetName", targetName).addProperty("serverName", serverName).addProperty("playerName", playerName).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        serverName = object.get("serverName").getAsString();
        targetName = object.get("targetName").getAsString();
        playerName = object.get("playerName").getAsString();
    }

    public String getServerName() {
        return serverName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTargetName() {
        return targetName;
    }
}
