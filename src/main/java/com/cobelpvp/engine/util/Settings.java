package com.cobelpvp.engine.util;

import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.Engine;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
public class Settings {

    private static Engine instance = Engine.getInstance();

    private Settings() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /* Settings Voids */
    public static String networkWebsite;
    public static String teamSpeak;
    public static String discord;
    public static String twitter;
    public static boolean clean;
    public static String networkStore;
    public static String serverName;
    public static boolean bannedPlayersCanJoin;
    public static String mongoUsername;
    public static boolean mongoOk;
    public static String mongoPassword;
    public static boolean redisOk;
    public static String redisPassword;

    /* Load Settings Data */
    public static void load() {
        twitter = "twitter.com/CobelPvPNetwork";
        networkWebsite = "www.cobelpvp.com";
        teamSpeak = "ts.cobelpvp.com";
        networkStore = "cobelpvp.tebex.io";
        discord = "https://discord.io/CobelPvP";
        serverName = "CobelPvP";
        clean = instance.getSettings().getBoolean("rankHandler.clean");
        redisPassword = instance.getSettings().getString("redis.password");
        redisOk = instance.getSettings().getBoolean("redis.auth");
        bannedPlayersCanJoin = instance.getSettings().getBoolean("banned-players.can-join");
        mongoPassword = instance.getSettings().getString("mongo.password");
        mongoUsername = instance.getSettings().getString("mongo.username");
        mongoOk = instance.getSettings().getBoolean("mongo.auth");
    }

    /* Save Settings Data */
    public void save() {
        instance.getSettings().set("mongo.Password", mongoPassword);
        instance.getSettings().set("mongo.Username", mongoUsername);
        instance.getSettings().set("mongo.Auth", mongoOk);
        instance.getSettings().set("rankHandler.clean", clean);
        instance.getSettings().set("banned-players.can-join", bannedPlayersCanJoin);
        instance.getSettings().set("redis.password", redisPassword);
        instance.getSettings().set("redis.auth", redisOk);
        instance.getSettings().save();
    }

    public static String getNetworkWebsite() {
        return networkWebsite;
    }

    public static String getTeamSpeak() {
        return teamSpeak;
    }

    public static String getDiscord() {
        return discord;
    }

    public static String getTwitter() {
        return twitter;
    }

    public static boolean isClean() {
        return clean;
    }

    public static String getNetworkStore() {
        return networkStore;
    }

    public static String getServerName() {
        return serverName;
    }

    public static boolean isBannedPlayersCanJoin() {
        return bannedPlayersCanJoin;
    }

    public static String getMongoUsername() {
        return mongoUsername;
    }

    public static boolean isMongoOk() {
        return mongoOk;
    }

    public static String getMongoPassword() {
        return mongoPassword;
    }

    public static boolean isRedisOk() {
        return redisOk;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }

    /* Utilities */
    public static String niceBuilder(Collection collection, String color) {
        return niceBuilder(collection, color + ", ", color + " and ", color + '.');
    }

    public static String niceBuilder(Collection collection, String delimiter, String and, String dot) {
        if (collection != null && !collection.isEmpty()) {
            List contents = new ArrayList(collection);
            String last = null;
            if (contents.size() > 1) {
                last = (String) contents.remove(contents.size() - 1);
            }

            StringBuilder builder = new StringBuilder();

            String name;
            for (Iterator iterator = contents.iterator(); iterator.hasNext(); builder.append(name)) {
                name = (String) iterator.next();
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
            }

            if (last != null) {
                builder.append(and).append(last);
            }

            return builder.append(dot != null ? dot : "").toString();
        } else {
            return "";
        }
    }
}
