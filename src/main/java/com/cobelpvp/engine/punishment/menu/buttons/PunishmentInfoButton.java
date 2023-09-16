package com.cobelpvp.engine.punishment.menu.buttons;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.util.TimeUtil;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedure;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedureStage;
import com.cobelpvp.engine.punishment.procedure.PunishmentProcedureType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PunishmentInfoButton extends Button {

    private Punishment punishment;
    private Profile profile;

    public PunishmentInfoButton(Punishment punishment, Profile profile) {
        this.punishment = punishment;
        this.profile = profile;
    }

    @Override
    public String getName(Player player) {
        return "" + ChatColor.GREEN + TimeUtil.dateToString(new Date(punishment.getAddedAt()));
    }

    @Override
    public List<String> getDescription(Player player) {
        String addedBy = "Console";

        if (punishment.getAddedBy() != null) {
            try {
                Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
                addedBy = addedByProfile.getDisplayName();
            } catch (Exception e) {
                addedBy = "Could not fetch...";
            }
        }

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
        lore.add("&eAdded by: &c" + addedBy);
        lore.add("&eAdded for: &c" + punishment.getAddedReason());

        if (!punishment.isPermanent() && punishment.getDuration() != -1) {
            lore.add("&eDuration: &c" + punishment.getTimeRemaining());
        }

        if (punishment.isRemoved()) {
            String removedBy = "Console";

            if (punishment.getRemovedBy() != null) {
                try {
                    Profile removedByProfile = Profile.getByUuid(punishment.getRemovedBy());
                    removedBy = removedByProfile.getDisplayName();
                } catch (Exception e) {
                    removedBy = "Could not fetch...";
                }
            }
            lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
            lore.add("&ePardoned at: &c" + TimeUtil.dateToString(new Date(punishment.getRemovedAt())));
            lore.add("&ePardoned by: &c" + removedBy);
            lore.add("&ePardoned for: &c" + punishment.getRemovedReason());
        }

        lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");

        if (!punishment.isRemoved() && punishment.getType().isRemovable()) {
            lore.add("&eRight click to pardon this punishment");
            lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
        }
        return ColorText.translate(lore);
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType == ClickType.RIGHT && !punishment.isRemoved() && punishment.getType().isRemovable()) {
            PunishmentProcedure procedure = new PunishmentProcedure(player, profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
            procedure.setPunishment(punishment);

            player.sendMessage(ChatColor.YELLOW + "Type reason for pardoning this punishment in chat...");
            player.closeInventory();
        }
    }
}