package com.cobelpvp.engine.grant;

import com.google.gson.JsonObject;
import com.cobelpvp.engine.json.JsonSerializer;

public class GrantJsonSerializer implements JsonSerializer<OGrant> {

    @Override
    public JsonObject serialize(OGrant grant) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", grant.getUuid().toString());
        object.addProperty("rank", grant.getRank().getUuid().toString());
        object.addProperty("addedBy", grant.getAddedBy() == null ? null : grant.getAddedBy().toString());
        object.addProperty("addedAt", grant.getAddedAt());
        object.addProperty("addedReason", grant.getAddedReason());
        object.addProperty("duration", grant.getDuration());
        object.addProperty("removedBy", grant.getRemovedBy() == null ? null : grant.getRemovedBy().toString());
        object.addProperty("removedAt", grant.getRemovedAt());
        object.addProperty("removedReason", grant.getRemovedReason());
        object.addProperty("removed", grant.isRemoved());
        return object;
    }
}
