package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.cobelpvp.bond.packet.Packet;
import com.cobelpvp.engine.json.JsonChain;

@Getter
public class StaffReportRedisCheck implements Packet {

    private String sentBy;
    private String accused;
    private String reason;
    private String serverId;
    private String serverName;
    private int reports;

    public StaffReportRedisCheck() {

    }

    public StaffReportRedisCheck(String sentBy, String accused, String reason, String serverId, String serverName, int reports) {
        this.sentBy = sentBy;
        this.accused = accused;
        this.reason = reason;
        this.serverId = serverId;
        this.serverName = serverName;
        this.reports = reports;
    }

    @Override
    public int id() {
        return 12;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("sentBy", sentBy)
                .addProperty("accused", accused)
                .addProperty("reason", reason)
                .addProperty("reports", reports)
                .addProperty("serverId", serverId)
                .addProperty("serverName", serverName)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        sentBy = object.get("sentBy").getAsString();
        accused = object.get("accused").getAsString();
        reports = object.get("reports").getAsInt();
        reason = object.get("reason").getAsString();
        serverId = object.get("serverId").getAsString();
        serverName = object.get("serverName").getAsString();
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getAccused() {
        return accused;
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

    public int getReports() {
        return reports;
    }
}