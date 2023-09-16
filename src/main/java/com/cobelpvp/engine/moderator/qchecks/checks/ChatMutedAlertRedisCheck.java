package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

@Getter
@Setter
public class ChatMutedAlertRedisCheck implements Packet {

    private String staff;
    private String status;
    private String serverName;

    public ChatMutedAlertRedisCheck() {

    }

    public ChatMutedAlertRedisCheck(String staff, String status, String serverName) {
        this.staff = staff;
        this.status = status;
        this.serverName = serverName;
    }

    @Override
    public int id() {
        return 18;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("serverName", serverName).addProperty("staff", staff).addProperty("status", status).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        serverName = object.get("serverName").getAsString();
        staff = object.get("staff").getAsString();
        status = object.get("status").getAsString();
    }

    public String getStaff() {
        return staff;
    }

    public String getStatus() {
        return status;
    }

    public String getServerName() {
        return serverName;
    }
}
