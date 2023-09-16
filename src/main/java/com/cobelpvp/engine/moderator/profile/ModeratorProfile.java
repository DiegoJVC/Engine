package com.cobelpvp.engine.moderator.profile;

import com.cobelpvp.engine.commands.staff.CmdSetFreezeSpawn;
import com.cobelpvp.engine.profile.Profile;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.util.ItemBuilder;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ModeratorProfile {

    @Getter
    private static Map<UUID, ModeratorProfile> qProfileMaps = new HashMap<>();
    private static MongoCollection<Document> QCProfile = Engine.getInstance().getModSuiteMongo().getCollection("profiles");
    private UUID uuid;
    private String name;
    private boolean staffModeEnabled;
    private boolean vanishEnabled;
    private boolean staffChatEnabled;
    private boolean freeze;
    private boolean loaded;

    public ModeratorProfile(String name, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
        load();
    }

    public void load() {
        Document QPDocumentLoad = QCProfile.find(Filters.eq("uuid", uuid.toString())).first();

        if (QPDocumentLoad != null) {
            if (name == null) {
                name = QPDocumentLoad.getString("playerName");
            }

            staffModeEnabled = QPDocumentLoad.getBoolean("staffMode");
            vanishEnabled = QPDocumentLoad.getBoolean("vanished");
            freeze = QPDocumentLoad.getBoolean("freeze");
            staffModeEnabled = QPDocumentLoad.getBoolean("staffChat");
        }

        loaded = true;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        Document QPDocument = new Document();
        QPDocument.put("playerName", name);
        QPDocument.put("uuid", uuid.toString());
        QPDocument.put("staffMode", staffModeEnabled);
        QPDocument.put("vanished", vanishEnabled);
        QPDocument.put("staffChat", staffChatEnabled);
        QPDocument.put("freeze", freeze);

        QCProfile.replaceOne(Filters.eq("uuid", uuid.toString()), QPDocument, new ReplaceOptions().upsert(true));
    }

    public void setVanished(boolean vanished) {
        this.vanishEnabled = vanished;

        Player player = getPlayer();

        if (vanished) {
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players.hasPermission("op")) {
                    return;
                }

                player.setMetadata("invisible", new FixedMetadataValue(Engine.getInstance(), true));
                players.hidePlayer(player);
            });
        } else {
            Bukkit.getOnlinePlayers().forEach(players -> {
                player.removeMetadata("invisible", Engine.getInstance());
                players.showPlayer(player);
            });
        }

        if (isStaffModeEnabled()) {
            if (vanished) {
                player.getInventory().setItem(7, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become  Visible").build());
            } else {
                player.getInventory().setItem(7, ItemBuilder.of(Material.INK_SACK).data((short) 10).name(ChatColor.YELLOW + "Become Invisible").build());
            }
        }
    }

    public void setStaffModeEnabled(boolean staffModeEnabled) {
        this.staffModeEnabled = staffModeEnabled;

        Player player = getPlayer();

        if (staffModeEnabled) {
            Engine.getArmor().put(player.getName(), player.getInventory().getArmorContents());
            Engine.getItems().put(player.getName(), player.getInventory().getContents());

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setGameMode(GameMode.CREATIVE);

            player.setMetadata("modmode", new FixedMetadataValue(Engine.getInstance(), true));

            setVanished(true);

            player.getInventory().setLeggings(ItemBuilder.of(Material.CHAINMAIL_LEGGINGS).build());
            player.getInventory().setItem(0, ItemBuilder.of(Material.COMPASS).name(ChatColor.YELLOW + "Compass").build());
            player.getInventory().setItem(1, ItemBuilder.of(Material.BOOK).name(ChatColor.YELLOW + "Interviewer").build());
            player.getInventory().setItem(2, ItemBuilder.of(Material.CARPET).data((short) 15).name(" ").build());
            player.getInventory().setItem(7, ItemBuilder.of(Material.INK_SACK).data((short) 8).name(ChatColor.YELLOW + "Become Visible").build());
            player.getInventory().setItem(8, ItemBuilder.of(Material.RECORD_10).name(ChatColor.YELLOW + "Random Teleport").build());
            player.getInventory().setItem(6, ItemBuilder.of(Material.WOOD_AXE).name(ChatColor.YELLOW + "World Edit").build());
        } else {
            setVanished(false);

            player.setGameMode(GameMode.SURVIVAL);

            player.removeMetadata("modmode", Engine.getInstance());

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setArmorContents(Engine.getArmor().get(player.getName()));
            player.getInventory().setContents(Engine.getItems().get(player.getName()));
        }
    }

    public static ModeratorProfile getPlayerByName(String username) {
        Player player = Bukkit.getPlayer(username);

        if (player != null) {
            return qProfileMaps.get(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

        if (offlinePlayer.hasPlayedBefore()) {
            if (qProfileMaps.containsKey(offlinePlayer.getUniqueId())) {
                return qProfileMaps.get(offlinePlayer.getUniqueId());
            }

            return new ModeratorProfile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }

        UUID uuid = TeamsUUIDCache.uuid(username);

        if (uuid != null) {
            if (qProfileMaps.containsKey(uuid)) {
                return qProfileMaps.get(uuid);
            }

            return new ModeratorProfile(username, uuid);
        }

        return null;
    }

    public static ModeratorProfile getPlayerByUuid(UUID uuid) {
        if (qProfileMaps.containsKey(uuid)) {
            return qProfileMaps.get(uuid);
        }

        return new ModeratorProfile(null, uuid);
    }

    public static Map<UUID, ModeratorProfile> getqProfileMaps() {
        return qProfileMaps;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isStaffModeEnabled() {
        return staffModeEnabled;
    }

    public boolean isVanishEnabled() {
        return vanishEnabled;
    }

    public boolean isStaffChatEnabled() {
        return staffChatEnabled;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVanishEnabled(boolean vanishEnabled) {
        this.vanishEnabled = vanishEnabled;
    }

    public void setStaffChatEnabled(boolean staffChatEnabled) {
        this.staffChatEnabled = staffChatEnabled;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;

        Profile profile = Profile.getByUsername(getPlayer().getName());
        if (freeze) {
            if (CmdSetFreezeSpawn.freezeSpawn == null) return;
            Location location = CmdSetFreezeSpawn.freezeSpawn;
            assert profile != null;
            profile.setLastFreezeLocation(profile.getPlayer().getLocation());
            getPlayer().teleport(location);
        } else {
            if (profile.getLastFreezeLocation() == null) return;
            profile.getPlayer().teleport(profile.getLastFreezeLocation());
            profile.getPlayer().sendMessage(ColorText.translate("&6Teleported to your last location"));
        }
    }
}
