package com.cobelpvp.engine.moderator.qchecks.checks;

import com.google.gson.JsonObject;
import com.cobelpvp.bond.packet.Packet;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.engine.punishment.Punishment;

public class PunishmentsAlertRedisCheck implements Packet {

    private String staffName;
    private String targetName;
    private String staffServerName;
    private String targetServerName;
    private String reason;
    private String duration;
    private int ips;
    private Punishment punishment;

    public PunishmentsAlertRedisCheck() {

    }

    public PunishmentsAlertRedisCheck(String staffName, String targetName, String staffServerName, String targetServerName, String reason, String duration, int ips, Punishment punishment) {
        this.staffName = staffName;
        this.targetName = targetName;
        this.staffServerName = staffServerName;
        this.targetServerName = targetServerName;
        this.reason = reason;
        this.duration = duration;
        this.ips = ips;
        this.punishment = punishment;
    }

    @Override
    public int id() {
        return 76;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().add("punishment", Punishment.SERIALIZER.serialize(punishment)).addProperty("ips", ips).addProperty("staffName", staffName).addProperty("targetName", targetName).addProperty("staffServerName", staffServerName).addProperty("targetServerName", targetServerName).addProperty("reason", reason).addProperty("duration", duration).get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        punishment = Punishment.DESERIALIZER.deserialize(jsonObject.get("punishment").getAsJsonObject());
        staffName = jsonObject.get("staffName").getAsString();
        targetName = jsonObject.get("targetName").getAsString();
        staffServerName = jsonObject.get("staffServerName").getAsString();
        targetServerName = jsonObject.get("targetServerName").getAsString();
        reason = jsonObject.get("reason").getAsString();
        duration = jsonObject.get("duration").getAsString();
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getStaffServerName() {
        return staffServerName;
    }

    public void setStaffServerName(String staffServerName) {
        this.staffServerName = staffServerName;
    }

    public String getTargetServerName() {
        return targetServerName;
    }

    public void setTargetServerName(String targetServerName) {
        this.targetServerName = targetServerName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIps() {
        return ips;
    }

    public void setIps(int ips) {
        this.ips = ips;
    }

    public Punishment getPunishment() {
        return punishment;
    }

    public void setPunishment(Punishment punishment) {
        this.punishment = punishment;
    }
}
