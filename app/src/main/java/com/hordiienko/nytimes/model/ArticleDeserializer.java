package com.hordiienko.nytimes.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ArticleDeserializer implements JsonDeserializer<Article> {
    @Override
    public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String url = getAsStringNullCheck(object.get("url"));
        String title = getAsStringNullCheck(object.get("title"));
        String description = getAsStringNullCheck(object.get("abstract"));
        String date = getAsStringNullCheck(object.get("published_date"));

        String image;

        try {
            JsonArray images = object.get("media").getAsJsonArray()
                    .get(0).getAsJsonObject()
                    .get("media-metadata").getAsJsonArray();

            image = images.size() > 0 ? images.get(images.size() - 1).getAsJsonObject()
                    .get("url").getAsString() : null;
        } catch (IllegalStateException e) {
            image = null;
        }

        return new Article(url, title, description, date, image);
    }

    private String getAsStringNullCheck(JsonElement element) {
        return element.isJsonNull() ? "" : element.getAsString();
    }
}
