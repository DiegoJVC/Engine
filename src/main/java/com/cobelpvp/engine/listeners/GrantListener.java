package com.cobelpvp.engine.listeners;

import com.cobelpvp.engine.checks.checks.DeleteGrantRedisCheck;
import com.cobelpvp.engine.grant.GrantAppliedEvent;
import com.cobelpvp.engine.grant.GrantExpireEvent;
import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.grant.procedure.GrantProcedure;
import com.cobelpvp.engine.grant.procedure.GrantProcedureStage;
import com.cobelpvp.engine.grant.procedure.GrantProcedureType;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.util.TimeUtil;
import com.cobelpvp.atheneum.menu.menus.ConfirmMenu;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GrantListener implements Listener {

    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        OGrant grant = event.getGrant();
        player.sendMessage(ColorText.translate("&6" + grant.getRank().getDisplayName() + " has applied to you for " + (grant.getDuration() == Integer.MAX_VALUE ? "forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) - System.currentTimeMillis()))));
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        OGrant grant = event.getGrant();
        player.sendMessage(ColorText.translate("&4Your rank named " + grant.getRank().getDisplayName() + " has expired"));
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("engine.grant.remove")) {
            return;
        }

        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                GrantProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(ChatColor.RED + "You have cancelled the grant procedure.");
                return;
            }

            if (procedure.getType() == GrantProcedureType.REMOVE) {
                new ConfirmMenu(ChatColor.YELLOW + "Delete this grant?", data -> {
                    if (data) {
                        procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                        procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                        procedure.getGrant().setRemovedReason(event.getMessage());
                        procedure.getGrant().setRemoved(true);
                        procedure.finish();
                        event.getPlayer().sendMessage(ChatColor.GREEN + "The grant has been removed.");

                        Engine.getInstance().getRedis().sendPacket(new DeleteGrantRedisCheck(procedure.getRecipient().getUuid(), procedure.getGrant()));
                    } else {
                        procedure.cancel();
                    }
                }).openMenu(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GrantProcedure procedure = GrantProcedure.getByPlayer(player);

        if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Delete this grant?")) {
            if (procedure.getType() == GrantProcedureType.REMOVE) {
                procedure.cancel();
                ((Player) event.getPlayer()).sendMessage(ChatColor.RED + "You did not confirm to remove the grant");
            }
        }
    }
}
