package com.cobelpvp.engine.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.checks.checks.RefreshRankRedisCheck;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Rank {

    /* Rank Voids */
    @Getter
    public static Map<UUID, Rank> ranksMap = new HashMap<>();
    public static MongoCollection<Document> collection;
    public final UUID uuid;
    public final String displayName;
    public String gamePrefix = "&a";
    public String gameColor = "&a";
    public int generalWeight, displayWeight;
    public boolean highRank, defaultRank;
    public final List<String> permissions = new ArrayList<>();
    public final List<Rank> inherited = new ArrayList<>();

    public Rank(String displayName) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;

        ranksMap.put(uuid, this);
    }

    public Rank(UUID uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;
        ranksMap.put(uuid, this);
    }

    public Rank(UUID uuid, String displayName, String gamePrefix, String gameColor, int generalWeight, int displayWeight, boolean defaultRank, boolean highRank) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.gamePrefix = gamePrefix;
        this.highRank = highRank;
        this.gameColor = gameColor;
        this.generalWeight = generalWeight;
        this.displayWeight = displayWeight;
        this.defaultRank = defaultRank;

        ranksMap.put(uuid, this);
    }

    public boolean hasPermission(String permission) {
        if (permission.contains(permission)) {
            return true;
        }

        for (Rank rank : inherited) {
            if (rank.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean rankCanInherit(Rank rankInherit) {
        if (inherited.contains(rankInherit) || rankInherit.inherited.contains(this)) {
            return false;
        }

        for (Rank rank : inherited) {
            if (!rank.rankCanInherit(rankInherit)) {
                return false;
            }
        }

        return true;
    }

    public List<String> getRankPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.addAll(this.permissions);

        for (Rank rank : inherited) {
            permissions.addAll(rank.getRankPermissions());
        }

        return permissions;
    }

    public void load() {
        load(collection.find(Filters.eq("id", uuid.toString())).first());
    }

    private void load(Document document) {
        if (document == null) {
            return;
        }

        gamePrefix = ColorText.translate(document.getString("gamePrefix"));
        gameColor = ColorText.translate(document.getString("gameColor"));
        generalWeight = document.getInteger("generalWeight");
        displayWeight = document.getInteger("displayWeight");
        highRank = document.getBoolean("highRank");
        defaultRank = document.getBoolean("defaultRank");
        permissions.clear();
        permissions.addAll(Engine.GSON.<List<String>>fromJson(document.getString("permissions"), Engine.LIST_STRING_TYPE));
        inherited.clear();
        Engine.GSON.<List<String>>fromJson(document.getString("inherits"), Engine.LIST_STRING_TYPE).stream().map(s -> Rank.getRankByID(UUID.fromString(s))).filter(Objects::nonNull).forEach(inherited::add);
    }

    public void save() {
        Document document = new Document();
        document.put("id", uuid.toString());
        document.put("displayName", displayName);
        document.put("gamePrefix", ColorText.translate(gamePrefix));
        document.put("highRank", highRank);
        document.put("gameColor", ColorText.translate(gameColor));
        document.put("generalWeight", generalWeight);
        document.put("displayWeight", displayWeight);
        document.put("defaultRank", defaultRank);
        document.put("permissions", Engine.GSON.toJson(permissions));
        document.put("inherits", Engine.GSON.toJson(inherited.stream().map(Rank::getUuid).map(UUID::toString).collect(Collectors.toList())));
        collection.replaceOne(Filters.eq("id", uuid.toString()), document, new ReplaceOptions().upsert(true));
        Engine.getInstance().getRedis().sendPacket(new RefreshRankRedisCheck(uuid, displayName));
    }

    public void deleteRank() {
        ranksMap.remove(uuid);
        collection.deleteOne(Filters.eq("id", uuid.toString()));
    }

    public void refresh() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (this.equals(profile.getActiveRank())) {
                profile.setupBukkitPlayer(player);
                player.setPlayerListName(profile.getActiveRank().getGameColor() + player.getName());
            }
        }
    }

    public static void init() {
        collection = Engine.getInstance().getMongoDatabase().getCollection("ranks");

        Map<Rank, List<UUID>> inheritanceReferences = new HashMap<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                Rank rank = new Rank(UUID.fromString(document.getString("id")), document.getString("displayName"));
                rank.load(document);
                rank.inherited.clear();

                List<UUID> ranksToInherit = new ArrayList<>();

                Engine.GSON.<List<String>>fromJson(document.getString("inherits"), Engine.LIST_STRING_TYPE).stream().map(UUID::fromString).forEach(ranksToInherit::add);

                inheritanceReferences.put(rank, ranksToInherit);

                ranksMap.put(rank.getUuid(), rank);
            }
        }

        inheritanceReferences.forEach((rank, list) -> {
            list.forEach(uuid -> {
                Rank inherited = ranksMap.get(uuid);

                if (inherited != null) {
                    rank.getInherited().add(inherited);
                }
            });
        });

        defaultRank();
    }

    public static Rank getRankByID(UUID id) {
        return ranksMap.get(id);
    }

    public static Rank getRankByDisplayName(String displayName) {
        for (Rank rank : ranksMap.values()) {
            if (rank.getDisplayName().equalsIgnoreCase(displayName)) {
                return rank;
            }
        }

        return null;
    }

    public static Rank defaultRank() {
        for (Rank rank : ranksMap.values()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }

        Rank rank = new Rank("Default");
        rank.setDefaultRank(true);
        rank.setHighRank(false);
        rank.save();

        ranksMap.put(rank.getUuid(), rank);

        return rank;
    }

    public String getFormattedName() {
        return this.getGameColor() + this.getDisplayName();
    }

    public void setGamePrefix(String gamePrefix) {
        this.gamePrefix = gamePrefix;
    }

    public void setGameColor(String gameColor) {
        this.gameColor = gameColor;
    }

    public void setGeneralWeight(int generalWeight) {
        this.generalWeight = generalWeight;
    }

    public void setDisplayWeight(int displayWeight) {
        this.displayWeight = displayWeight;
    }

    public void setHighRank(boolean highRank) {
        this.highRank = highRank;
    }

    public void setDefaultRank(boolean defaultRank) {
        this.defaultRank = defaultRank;
    }

    public static Map<UUID, Rank> getRanksMap() {
        return ranksMap;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGamePrefix() {
        return Settings.isClean() ? ColorText.translate(this.gamePrefix.replace("&l", "")) : ColorText.translate(this.gamePrefix);
    }

    public String getGameColor() {
        return Settings.isClean() ? ColorText.translate(this.gameColor.replace("&l", "")) : ColorText.translate(this.gameColor);
    }

    public int getGeneralWeight() {
        return generalWeight;
    }

    public int getDisplayWeight() {
        return displayWeight;
    }

    public boolean isHighRank() {
        return highRank;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<Rank> getInherited() {
        return inherited;
    }
}
