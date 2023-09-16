package com.cobelpvp.engine.checks.checks;

import com.cobelpvp.engine.grant.OGrant;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.bond.packet.Packet;
import com.cobelpvp.engine.json.JsonChain;

import java.util.UUID;

@Getter
@Setter
public class AddGrantRedisCheck implements Packet {

    private UUID playerUuid;
    private OGrant grant;

    public AddGrantRedisCheck() {

    }

    public AddGrantRedisCheck(UUID playerUuid, OGrant grant) {
        this.playerUuid = playerUuid;
        this.grant = grant;
    }

    @Override
    public int id() {
        return 1;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("playerUuid", playerUuid.toString()).add("grant", OGrant.SERIALIZER.serialize(grant)).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        playerUuid = UUID.fromString(object.get("playerUuid").getAsString());
        grant = OGrant.DESERIALIZER.deserialize(object.get("grant").getAsJsonObject());
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public OGrant getGrant() {
        return grant;
    }
}
