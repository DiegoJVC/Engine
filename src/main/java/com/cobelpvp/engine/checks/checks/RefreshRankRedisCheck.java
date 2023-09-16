package com.cobelpvp.engine.checks.checks;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.json.JsonChain;
import com.cobelpvp.bond.packet.Packet;

import java.util.UUID;

@Getter
@Setter
public class RefreshRankRedisCheck implements Packet {

    private UUID uuid;
    private String name;

    public RefreshRankRedisCheck() {

    }

    public RefreshRankRedisCheck(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public int id() {
        return 6;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("uuid", uuid.toString())
                .addProperty("name", name)
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
        name = object.get("name").getAsString();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}