package com.cobelpvp.engine.grant;

import com.cobelpvp.engine.rank.Rank;
import com.google.gson.JsonObject;
import com.cobelpvp.engine.json.JsonDeserializer;

import java.util.UUID;

public class GrantJsonDeserializer implements JsonDeserializer<OGrant> {

    @Override
    public OGrant deserialize(JsonObject object) {
        Rank rank = Rank.getRankByID(UUID.fromString(object.get("rank").getAsString()));

        if (rank == null) {
            return null;
        }

        OGrant grant = new OGrant(UUID.fromString(object.get("uuid").getAsString()), rank, null, object.get("addedAt").getAsLong(), object.get("addedReason").getAsString(), object.get("duration").getAsLong());

        if (!object.get("addedBy").isJsonNull()) {
            grant.setAddedBy(UUID.fromString(object.get("addedBy").getAsString()));
        }

        if (!object.get("removedBy").isJsonNull()) {
            grant.setRemovedBy(UUID.fromString(object.get("removedBy").getAsString()));
        }

        if (!object.get("removedAt").isJsonNull()) {
            grant.setRemovedAt(object.get("removedAt").getAsLong());
        }

        if (!object.get("removedReason").isJsonNull()) {
            grant.setRemovedReason(object.get("removedReason").getAsString());
        }

        if (!object.get("removed").isJsonNull()) {
            grant.setRemoved(object.get("removed").getAsBoolean());
        }

        return grant;
    }

}