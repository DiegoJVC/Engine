package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
public class StaffRequestRedisCheck implements Packet {

    private String sentBy;
    private String reason;
    private String serverId;
    private String serverName;

    public StaffRequestRedisCheck() {

    }

    public StaffRequestRedisCheck(String sentBy, String reason, String serverId, String serverName) {
        this.sentBy = sentBy;
        this.reason = reason;
        this.serverId = serverId;
        this.serverName = serverName;
    }

    @Override
    public int id() {
        return 14;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("sentBy", sentBy)
                .addProperty("reason", reason)
                .addProperty("serverId", serverId)
                .addProperty("serverName", serverName)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        sentBy = object.get("sentBy").getAsString();
        reason = object.get("reason").getAsString();
        serverId = object.get("serverId").getAsString();
        serverName = object.get("serverName").getAsString();
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getReason() {
        return reason;
    }

    public String getServerId() {
        return serverId;
    }

    public String getServerName() {
        return serverName;
    }
}
