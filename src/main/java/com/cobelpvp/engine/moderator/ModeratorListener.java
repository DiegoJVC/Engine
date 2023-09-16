package com.cobelpvp.engine.moderator;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.moderator.qchecks.checks.StaffChatRedisCheck;
import com.cobelpvp.engine.moderator.menu.InvseeMenu;
import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.Random;

public class ModeratorListener implements Listener {

    @EventHandler
    public void onAsyncPlayerJoin(AsyncPlayerPreLoginEvent event) {
        ModeratorProfile suiteProfile = null;

        try {
            suiteProfile = new ModeratorProfile(event.getName(), event.getUniqueId());
            if (!suiteProfile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("Failed to load your moderator profile");
                return;
            }

            suiteProfile.setName(event.getName());

            suiteProfile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (suiteProfile == null || !suiteProfile.isLoaded()) {
            event.setKickMessage(ChatColor.RED + "Failed to load your profile");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        ModeratorProfile.getqProfileMaps().put(suiteProfile.getUuid(), suiteProfile);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (suiteProfile == null || !suiteProfile.isLoaded()) return;

        if (suiteProfile.isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (suiteProfile == null || !suiteProfile.isLoaded()) return;

        if (suiteProfile.isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStaffAsyncChat(AsyncPlayerChatEvent event) {
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByUuid(event.getPlayer().getUniqueId());
        Profile profile = Profile.getByUsername(event.getPlayer().getName());

        if (profile == null || !profile.isLoaded()) return;

        if (suiteProfile.isStaffChatEnabled()) {
            Engine.getInstance().getRedis().sendPacket(new StaffChatRedisCheck(profile.getColoredUsername(), Bukkit.getServerName(), event.getMessage()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(player.getName());

            if (suiteProfile == null || !suiteProfile.isLoaded()) return;

            if (suiteProfile.isStaffModeEnabled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(event.getPlayer().getUniqueId()));

        if (profile == null || !profile.isLoaded()) return;

        if (modSuiteProfile == null || !modSuiteProfile.isLoaded()) {
            player.kickPlayer(ChatColor.RED + "Failed to load your profile");
            System.out.println("Failed to load profile of " + profile.getDisplayName() + " kicked this.");
        }

        if (player.hasPermission("moderator.staff")) {
            if (Bukkit.getServerName().contains("Practice")) return;
            if (Bukkit.getServerName().contains("Hub")) return;
            if (Bukkit.getServerName().contains("KitPvP")) return;
            if (Bukkit.getServerName().contains("SkyWars")) return;
            if (Bukkit.getServerName().contains("PrisonOP")) return;

            assert modSuiteProfile != null;
            modSuiteProfile.setStaffModeEnabled(true);
        }

        for (Player players : Bukkit.getOnlinePlayers()) {
            ModeratorProfile staffProfile = ModeratorProfile.getPlayerByName(players.getName());

            if (staffProfile == null || !staffProfile.isLoaded()) return;

            if (staffProfile.isVanishEnabled()) {
                if (player.hasPermission("moderator.staff")) return;
                player.hidePlayer(players);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());

        if (modSuiteProfile == null || !modSuiteProfile.isLoaded()) return;

        if (modSuiteProfile.isVanishEnabled()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();

                if (block.getState() instanceof ContainerBlock) {
                    event.setCancelled(true);
                    ContainerBlock containerBlock = (ContainerBlock) block.getState();
                    if (block.getState() instanceof Furnace) return;
                    Inventory inventory = Bukkit.createInventory(null, containerBlock.getInventory().getSize(), ChatColor.YELLOW + "Silently Chest...");

                    for (int i = 0; i < containerBlock.getInventory().getSize(); i++) {
                        inventory.setItem(i, containerBlock.getInventory().getItem(i));
                    }

                    player.openInventory(inventory);
                }
            }
        }

        if (modSuiteProfile.isStaffModeEnabled()) {
            if (player.getItemInHand().getType() == Material.INK_SACK) {
                boolean toggle = !modSuiteProfile.isVanishEnabled();
                modSuiteProfile.setVanished(toggle);
            }
        }

        if (player.getItemInHand().getType() == Material.RECORD_10) {
            teleportToRandom(player);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

        if (profile == null || !profile.isLoaded()) return;

        if (profile.isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractByEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

        if (profile == null || !profile.isLoaded()) return;

        if (profile.isStaffModeEnabled()) {
            if (event.getRightClicked() instanceof Player) {
                Player target = (Player) event.getRightClicked();
                if (player.getItemInHand().getType() == Material.BOOK) {
                    new InvseeMenu(target).openMenu(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

        if (profile == null || !profile.isLoaded()) return;

        if (profile.isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

        if (profile == null || !profile.isLoaded()) return;

        if (profile.isStaffModeEnabled()) {
            if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
                return;

            if (event.getClickedInventory().getTitle().equals(ChatColor.YELLOW + "Silently Chest...")) {
                event.setCancelled(true);
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

            if (profile == null || !profile.isLoaded()) return;

            if (profile.isStaffModeEnabled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            Player player = event.getEntity();
            ModeratorProfile profile = ModeratorProfile.getPlayerByName(player.getName());

            if (profile == null || !profile.isLoaded()) return;

            if (profile.isStaffModeEnabled()) {
                event.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        ModeratorProfile suiteProfile = ModeratorProfile.getqProfileMaps().get(event.getPlayer().getUniqueId());

        if (suiteProfile == null || !suiteProfile.isLoaded()) return;


        if (!(Bukkit.getServerName().contains("Hub") || Bukkit.getServerName().contains("Practice") || Bukkit.getServerName().contains("KitPvP"))) {
            if (suiteProfile.isStaffModeEnabled()) {
                suiteProfile.setStaffModeEnabled(false);
            }
        }

        if (suiteProfile.isLoaded()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    suiteProfile.save();
                }
            }.runTaskAsynchronously(Engine.getInstance());
        }
    }

    public void teleportToRandom(Player player) {
        Random random = new Random();
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        Player randomPlayer = players.get(random.nextInt(players.size()));
        if (Bukkit.getOnlinePlayers().size() == 1) {
            randomPlayer.sendMessage(ChatColor.RED + "There are not enough players to use this.");
        }
        if (Bukkit.getOnlinePlayers().size() > 1) {
            if (randomPlayer == player) return;
            player.teleport(randomPlayer);
        }
    }
}