package com.cobelpvp.engine.punishment;

import com.cobelpvp.atheneum.menu.menus.ConfirmMenu;
import com.cobelpvp.atheneum.util.Callback;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedure;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedureStage;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedureType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("engine.staff.grant")) {
            return;
        }

        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                PunishmentProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(ChatColor.RED + "You have cancelled the punishment procedure.");
                return;
            }

            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                new ConfirmMenu(ChatColor.YELLOW + "Pardon this punishment?", new Callback<Boolean>() {
                    @Override
                    public void callback(Boolean data) {
                        if (data) {
                            procedure.getPunishment().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getPunishment().setRemovedAt(System.currentTimeMillis());
                            procedure.getPunishment().setRemovedReason(event.getMessage());
                            procedure.getPunishment().setRemoved(true);
                            procedure.finish();

                            event.getPlayer().sendMessage(ChatColor.GREEN + "The punishment has been removed.");
                        } else {
                            procedure.cancel();
                            event.getPlayer().sendMessage(ChatColor.RED + "You did not confirm to pardon the punishment.");
                        }
                    }
                }).openMenu(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(player);

        if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Pardon this punishment?")) {
            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                procedure.cancel();
                player.sendMessage(ChatColor.RED + "You did not confirm to pardon the punishment.");
            }
        }
    }
}
