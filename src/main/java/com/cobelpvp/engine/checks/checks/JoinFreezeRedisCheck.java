package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
@Setter
public class JoinFreezeRedisCheck implements Packet {

    private String serverName;
    private String playerName;

    public JoinFreezeRedisCheck() {

    }

    public JoinFreezeRedisCheck(String playerName, String serverName) {
        this.serverName = serverName;
        this.playerName = playerName;
    }

    @Override
    public int id() {
        return 25;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("serverName", serverName).addProperty("playerName", playerName).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        serverName = object.get("serverName").getAsString();
        playerName = object.get("playerName").getAsString();
    }

    public String getServerName() {
        return serverName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
