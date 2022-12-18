package io.devnindo.service.util;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.service.deploy.RuntimeMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ServiceConfigUtil
{
    public static final JsonObject readIdentityConfig() throws IOException {
        String path =  "config/identity.json";
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return new JsonObject(content);
    }
    public static final JsonObject readConfig(RuntimeMode runtimeMode, String componentName$) throws IOException {
        String path =  "config/" + runtimeMode+"/" + componentName$+".json";
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        return new JsonObject(content);
    }

    public static io.vertx.core.json.JsonObject toVertxJS(JsonObject obj){
        return new io.vertx.core.json.JsonObject(obj.getMap());
    }
}