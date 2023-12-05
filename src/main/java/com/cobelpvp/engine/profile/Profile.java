package com.cobelpvp.engine.profile;

import com.cobelpvp.engine.grant.GrantAppliedEvent;
import com.cobelpvp.engine.grant.GrantExpireEvent;
import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.engine.util.LocationUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

@Getter
@Setter
public class Profile {

    @Getter private static Map<UUID, Profile> profileMap = new HashMap<>();
    private static MongoCollection<Document> collection = Engine.getInstance().getMongoDatabase().getCollection("profiles");
    private final UUID uuid;
    private boolean loaded;
    private String displayName;
    private String address;
    private String lastServer;
    private String country;
    private List<String> alts;
    private List<String> ipAddresses;
    private Long firstSeen;
    private Long lastSeen;
    private final ProfileOptions options;
    private final ProfileConversations conversations;
    private Location lastFreezeLocation;
    private int reports;
    private OGrant activeGrant;
    private List<String> permissions;
    private final List<OGrant> grants;
    private final List<Punishment> punishments;

    public Profile(String displayName, UUID uuid) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.alts = new ArrayList<>();
        this.grants = new ArrayList<>();
        this.punishments = new ArrayList<>();
        this.options = new ProfileOptions();
        this.permissions = new ArrayList<>();
        this.conversations = new ProfileConversations(this);
        this.ipAddresses = new ArrayList<>();

        load();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getColoredUsername() {
        return activeGrant.getRank().getGameColor() + displayName;
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && !punishment.isRemoved() && !punishment.hasExpired()) {
                return punishment;
            }
        }

        return null;
    }

    public void addAlt(Profile altProfile) {
        if (!this.alts.contains(altProfile.getUuid().toString())) {
            this.alts.add(altProfile.getUuid().toString());
        }
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;

        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) i++;
        }

        return i;
    }

    public Rank getActiveRank() {
        return activeGrant.getRank();
    }

    public void activateNextGrant() {
        List<OGrant> grants = new ArrayList<>(this.grants);

        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getGeneralWeight()));
        Collections.reverse(grants);

        for (OGrant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                if (!grant.equals(activeGrant)) {
                    activeGrant = grant;
                    setupBukkitPlayer(getPlayer());
                    return;
                }
            }
        }
    }

    public void checkGrants() {
        Player player = getPlayer();
        for (OGrant grant : grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);

                if (player != null) {
                    new GrantExpireEvent(player, grant).call();
                }
            }
        }
        if (activeGrant != null && activeGrant.isRemoved()) activateNextGrant();
        if (activeGrant == null) {
            OGrant defaultGrant = new OGrant(UUID.randomUUID(), Rank.defaultRank(), null, System.currentTimeMillis(), "Default", Integer.MAX_VALUE);
            grants.add(defaultGrant);
            activeGrant = defaultGrant;
            if (player != null) {
                setupBukkitPlayer(getPlayer());
                new GrantAppliedEvent(player, defaultGrant).call();
            }
        }
    }

    public void load() {
        Document document = collection.find(Filters.eq("id", uuid.toString())).first();

        if (document != null) {
            if (displayName == null) displayName = document.getString("displayName");
            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            address = document.getString("address");
            reports = document.getInteger("reports");
            if (document.getString("lastFreezeLocation") != null) lastFreezeLocation = LocationUtil.deserialize(document.getString("lastFreezeLocation"));
            lastServer = document.getString("lastServer");
            country = document.getString("country");
            ipAddresses = Engine.GSON.fromJson(document.getString("ipAddresses"), Engine.LIST_STRING_TYPE);
            alts = document.get("alts", List.class);
            if (alts == null) {
                alts = new ArrayList<>();
            }

            if (document.getString("permissions") != null) {
                permissions = Engine.GSON.fromJson(document.getString("permissions"), Engine.LIST_STRING_TYPE);
            }

            Document optionsDocument = (Document) document.get("options");
            if (document.containsKey("options")) {
                options.setPublicChatEnabled(optionsDocument.getBoolean("publicChatEnabled"));
                if (optionsDocument.containsKey("receivingNewConversations_1")) {
                    options.setReceivingNewConversations(optionsDocument.getBoolean("receivingNewConversations_1"));
                } else {
                    options.setReceivingNewConversations(true);
                }
                if (optionsDocument.containsKey("playingMessageSounds")) {
                    options.setPlayingMessageSounds(optionsDocument.getBoolean("playingMessageSounds"));
                } else {
                    options.setPlayingMessageSounds(true);
                }
            }

            JsonArray grantList = new JsonParser().parse(document.getString("grants")).getAsJsonArray();

            for (JsonElement grantData : grantList) {
                OGrant grant = OGrant.DESERIALIZER.deserialize(grantData.getAsJsonObject());

                if (grant != null) {
                    this.grants.add(grant);
                }
            }

            JsonArray punishmentList = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();

            for (JsonElement punishmentData : punishmentList) {
                Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

                if (punishment != null) this.punishments.add(punishment);
            }
        }

        activateNextGrant();
        checkGrants();
        loaded = true;
    }

    public void save() {
        Document document = new Document();
        document.put("displayName", displayName);
        document.put("id", uuid.toString());
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastServer", lastServer);
        document.put("reports", reports);
        document.put("address", address);
        document.put("ipAddresses", Engine.GSON.toJson(ipAddresses, Engine.LIST_STRING_TYPE));
        document.put("country", country);

        Document optionsDocument = new Document();
        optionsDocument.put("publicChatEnabled", options.isPublicChatEnabled());
        optionsDocument.put("receivingNewConversations_1", options.isReceivingNewConversations());
        optionsDocument.put("playingMessageSounds", options.isPlayingMessageSounds());
        document.put("options", optionsDocument);

        if (lastFreezeLocation != null) document.put("lastFreezeLocation", LocationUtil.serialize(lastFreezeLocation));

        document.append("alts", this.alts);
        JsonArray grantList = new JsonArray();

        for (OGrant grant : this.grants) grantList.add(OGrant.SERIALIZER.serialize(grant));

        document.put("grants", grantList.toString());
        JsonArray punishmentList = new JsonArray();

        for (Punishment punishment : this.punishments) punishmentList.add(Punishment.SERIALIZER.serialize(punishment));

        document.put("permissions", Engine.GSON.toJson(permissions, Engine.LIST_STRING_TYPE));

        document.put("punishments", punishmentList.toString());
        collection.replaceOne(Filters.eq("id", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }


    public void setupBukkitPlayer(Player player) {
        if (player == null) return;
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null || !attachmentInfo.getAttachment().getPlugin().equals(Engine.getInstance())) continue;

            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment attachment = player.addAttachment(Engine.getInstance());

        for (String permission : activeGrant.getRank().getRankPermissions()) attachment.setPermission(permission, true);

        player.recalculatePermissions();

        for (String permission : permissions) {
            attachment.setPermission(permission, true);
        }

        String displayName = activeGrant.getRank().getGamePrefix() + player.getName();
        String coloredName = getActiveGrant().getRank().getGameColor() + player.getName();

        if (coloredName.length() > 16) coloredName = coloredName.substring(0, 15);

        player.setDisplayName(displayName);

        player.setPlayerListName(coloredName);
    }

    public void updatePermissions() {
        Player player = Bukkit.getPlayer(uuid);
        // Clear any permissions set for this player by this plugin
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null ||
                    !attachmentInfo.getAttachment().getPlugin().equals(Engine.getInstance())) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions()
                    .forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(Engine.getInstance());

        for (String permission : activeGrant.getRank().getRankPermissions()) {
            attachment.setPermission(permission, true);
        }

        for (String permission : permissions) {
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }

    public static Profile getByUuid(UUID uuid) {
        if (profileMap.containsKey(uuid)) {
            return profileMap.get(uuid);
        }

        return new Profile(null, uuid);
    }

    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);

        if (player != null) {
            return profileMap.get(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

        if (offlinePlayer.hasPlayedBefore()) {
            if (profileMap.containsKey(offlinePlayer.getUniqueId())) {
                return profileMap.get(offlinePlayer.getUniqueId());
            }
            return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }

        UUID uuid = TeamsUUIDCache.uuid(username);

        if (uuid != null) {
            if (profileMap.containsKey(uuid)) {
                return profileMap.get(uuid);
            }
            return new Profile(username, uuid);
        }

        return null;
    }

    public static List<Profile> getByIpAddress(String ipAddress) {
        List<Profile> profiles = new ArrayList<>();
        Bson filter = Filters.eq("address", ipAddress);
        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                profiles.add(new Profile(document.getString("displayName"), UUID.fromString(document.getString("id"))));
            }
        }

        return profiles;
    }
}
