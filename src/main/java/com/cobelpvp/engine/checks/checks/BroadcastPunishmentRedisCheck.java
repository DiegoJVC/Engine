package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.bond.packet.Packet;

import java.util.UUID;

@Getter
@Setter
public class BroadcastPunishmentRedisCheck implements Packet {

    private Punishment punishment;
    private String staff;
    private String target;
    private UUID targetUuid;

    public BroadcastPunishmentRedisCheck() {

    }

    public BroadcastPunishmentRedisCheck(Punishment punishment, String staff, String target, UUID targetUuid) {
        this.punishment = punishment;
        this.staff = staff;
        this.target = target;
        this.targetUuid = targetUuid;
    }

    @Override
    public int id() {
        return 2;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().add("punishment", Punishment.SERIALIZER.serialize(punishment)).addProperty("staff", staff).addProperty("target", target).addProperty("targetUuid", targetUuid.toString()).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        punishment = Punishment.DESERIALIZER.deserialize(object.get("punishment").getAsJsonObject());
        staff = object.get("staff").getAsString();
        target = object.get("target").getAsString();
        targetUuid = UUID.fromString(object.get("targetUuid").getAsString());
    }

    public Punishment getPunishment() {
        return punishment;
    }

    public String getStaff() {
        return staff;
    }

    public String getTarget() {
        return target;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }
}