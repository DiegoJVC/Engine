package com.cobelpvp.engine.json;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {

    T deserialize(JsonObject object);

}
