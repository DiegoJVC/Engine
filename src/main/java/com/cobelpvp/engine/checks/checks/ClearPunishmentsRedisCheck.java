package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

import java.util.UUID;

@Getter
public class ClearPunishmentsRedisCheck implements Packet {

    private UUID uuid;

    public ClearPunishmentsRedisCheck() {

    }

    public ClearPunishmentsRedisCheck(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int id() {
        return 4;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", uuid.toString()).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
    }

    public UUID getUuid() {
        return uuid;
    }
}
