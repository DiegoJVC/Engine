package com.cobelpvp.engine;

import com.cobelpvp.engine.checks.RedisChecksListener;
import com.cobelpvp.engine.checks.checks.*;
import com.cobelpvp.engine.commands.parameter.EnchantParameterType;
import com.cobelpvp.engine.commands.parameter.GameModeParameterType;
import com.cobelpvp.engine.commands.staff.CmdSetSlots;
import com.cobelpvp.engine.holograms.HologramManager;
import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import com.cobelpvp.engine.moderator.qchecks.RedisQChecksListener;
import com.cobelpvp.engine.moderator.qchecks.checks.*;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.exploit.config.ConfigLoader;
import com.cobelpvp.engine.exploit.config.ConfigValues;
import com.cobelpvp.engine.exploit.util.filter.FilterManager;
import com.cobelpvp.engine.exploit.impl.HookManager;
import com.cobelpvp.engine.exploit.util.ChatListener;
import com.cobelpvp.engine.util.Helper;
import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.engine.tasks.AnnouncementTask;
import com.cobelpvp.engine.util.ListenerHandler;
import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.engine.util.TaskUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import com.cobelpvp.bond.Bond;
import com.cobelpvp.engine.chat.Chat;
import com.cobelpvp.atheneum.command.TeamsCommandHandler;
import com.cobelpvp.atheneum.util.Config;
import com.cobelpvp.atheneum.util.Cooldowns;
import lombok.Setter;
import net.minecraft.server.v1_7_R4.Packet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;

@Getter
public class Engine extends JavaPlugin {

    @Getter private ArrayList<Packet> blockedPackets = new ArrayList<>();
    @Getter @Setter
    public boolean togglePacketLogger = false;
    private static Engine instance;
    public static final Gson GSON = new Gson();
    private Bond redis;
    private Chat chat;
    private JedisPool jedisPool;
    private MongoDatabase mongoDatabase;
    private Config settings;
    private final ConfigValues configValues = new ConfigValues();
    private ProtocolManager protocolManager;
    final double speed = getConfig().getDouble("speed");
    private static Map<String, ItemStack[]> armor;
    private static Map<String, ItemStack[]> items;
    private MongoDatabase modSuiteMongo;
    private HologramManager hologramManager;
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

    public Engine() {
        armor = new HashMap<>();
        items = new HashMap<>();
    }

    @Override
    public void onEnable() {
        instance = this;
        settings = new Config(this, "config.yml");
        CmdSetSlots.load();

        if (Settings.mongoOk) {
            mongoDatabase = new MongoClient(new ServerAddress("127.0.0.1", 27017), MongoCredential.createCredential(Settings.mongoUsername, "Engine", Settings.mongoPassword.toCharArray()), MongoClientOptions.builder().build()).getDatabase("Engine");
        } else {
            mongoDatabase = new MongoClient("127.0.0.1", 27017).getDatabase("Engine");
        }

        loadModSuiteMongo();

        jedisPool = new JedisPool("127.0.0.1", 6379);

        if (Settings.redisOk) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(Settings.redisPassword);
            }
        }

        Cooldowns.createCooldown("report");
        Cooldowns.createCooldown("helpop");
        Cooldowns.createCooldown("globalChatCooldown");
        Cooldowns.createCooldown("vipannouncement");

        TeamsCommandHandler.registerAll(this);
        TeamsCommandHandler.registerParameterType(GameMode.class, new GameModeParameterType());
        TeamsCommandHandler.registerParameterType(Enchantment.class, new EnchantParameterType());

        redis = new Bond("Engine", "127.0.0.1", 6379, Settings.redisOk ? Settings.redisPassword : null);

        Arrays.asList(
                StaffRequestRedisCheck.class,
                StaffReportRedisCheck.class,
                ChatMutedAlertRedisCheck.class,
                StaffLeaveRedisCheck.class,
                StaffChatRedisCheck.class,
                QuitFreezeRedisCheck.class,
                StaffJoinRedisCheck.class,
                BanFailedRedisCheck.class,
                PunishmentsAlertRedisCheck.class,
                VIPAnnouncementCheck.class,
                ServerStatusRedisCheck.class,
                AddGrantRedisCheck.class,
                BroadcastPunishmentRedisCheck.class,
                JoinFreezeRedisCheck.class,
                DeleteGrantRedisCheck.class,
                RefreshRankRedisCheck.class,
                ClearGrantsRedisCheck.class,
                ClearPunishmentsRedisCheck.class)
                .forEach(redis::registerPacket);

        chat = new Chat();
        new AnnouncementTask();

        new ConfigLoader().load();
        new Helper();

        if (this.configValues.isUsePackets()) {
            new HookManager();
        }

        this.hologramManager = new HologramManager();

        if (this.configValues.isUseFilter()) {
            new FilterManager().setupFilter();
        }

        getServer().getPluginManager().registerEvents(new ChatListener(), this);


        redis.registerListener(new RedisQChecksListener());
        redis.registerListener(new RedisChecksListener());

        Rank.init();
        new Config(this, "ranksForImport");

        TaskUtil.runTaskTimer(() -> {
            for (Player players : Bukkit.getOnlinePlayers()) {
                try {
                    Profile profile = Profile.getByUsername(players.getName());
                    assert profile != null;
                    assert profile.isLoaded();
                    profile.checkGrants();
                } catch (Exception e) {

                }
            }
        }, 0, 0);

        ListenerHandler.loadListenersFromPackage(this, "com.cobelpvp.engine.listeners");
        ListenerHandler.loadListenersFromPackage(this, "com.cobelpvp.engine.moderator");
        ListenerHandler.loadListenersFromPackage(this, "com.cobelpvp.engine.profile");

        Settings.load();

        (this.protocolManager = ProtocolLibrary.getProtocolManager()).addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
                    try {
                        PacketContainer packet = event.getPacket();
                        String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
                        if ((message.startsWith("/") && !message.contains(" "))) {
                            event.setCancelled(true);
                        }
                    }
                    catch (FieldAccessException e) {
                        getLogger().log(Level.SEVERE, "Couldn't access field.", e);
                    }
                }
            }
        });

        getRedis().sendPacket(new ServerStatusRedisCheck(Bukkit.getServerName(), "online"));
    }

    @Override
    public void onDisable() {
        getRedis().sendPacket(new ServerStatusRedisCheck(Bukkit.getServerName(), "offline"));

        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(players.getName());
            assert suiteProfile != null;
            if (suiteProfile.isStaffModeEnabled()) {
                suiteProfile.setStaffModeEnabled(false);
            }

            Profile profile = Profile.getByUuid(players.getUniqueId());
            try {
                profile.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModSuiteMongo() {
        if (Settings.mongoOk) {
            modSuiteMongo = new MongoClient(new ServerAddress("127.0.0.1", 27017), MongoCredential.createCredential(Settings.mongoUsername, "Moderator", Settings.mongoPassword.toCharArray()), MongoClientOptions.builder().build()).getDatabase("Moderator");
        } else {
            modSuiteMongo = new MongoClient("127.0.0.1", 27017).getDatabase("Moderator");
        }
    }

    public static Map<String, ItemStack[]> getArmor() {
        return armor;
    }

    public static Map<String, ItemStack[]> getItems() {
        return items;
    }

    public static Engine getInstance() {
        return instance;
    }

    public Bond getRedis() {
        return redis;
    }

    public Chat getChat() {
        return chat;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public HologramManager getHologramManager() {
        return this.hologramManager;
    }

    public Config getSettings() {
        return settings;
    }

    public MongoDatabase getModSuiteMongo() {
        return modSuiteMongo;
    }
}
