package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import com.cobelpvp.bond.packet.Packet;
import com.cobelpvp.engine.json.JsonChain;

public class ServerStatusRedisCheck implements Packet {

    private String serverName;
    private String status;

    public ServerStatusRedisCheck() {

    }

    public ServerStatusRedisCheck(String serverName, String status) {
        this.serverName = serverName;
        this.status = status;
    }

    @Override
    public int id() {
        return 79;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("serverName", serverName).addProperty("status", status).get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        serverName = jsonObject.get("serverName").getAsString();
        status = jsonObject.get("status").getAsString();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
