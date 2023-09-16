package com.cobelpvp.engine.grant.menu;

import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.grant.procedure.GrantProcedure;
import com.cobelpvp.engine.grant.procedure.GrantProcedureStage;
import com.cobelpvp.engine.grant.procedure.GrantProcedureType;
import com.cobelpvp.engine.util.TimeUtil;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.menu.pagination.PaginatedMenu;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.profile.Profile;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public class GrantsListMenu extends PaginatedMenu {

    private static final List COLORS = new ArrayList(Arrays.asList(ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.BLACK, ChatColor.DARK_GREEN, ChatColor.RED));
    private Profile profile;

    public GrantsListMenu(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ColorText.translate("&6Grants");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (OGrant grant : profile.getGrants()) {
            buttons.put(buttons.size(), new GrantInfoButton(profile, grant));
        }

        return buttons;
    }

    private class GrantInfoButton extends Button {

        private Profile profile;
        private OGrant grant;

        public GrantInfoButton(Profile profile, OGrant grant) {
            this.profile = profile;
            this.grant = grant;
        }

        @Override
        public String getName(Player player) {
            return ChatColor.GREEN + TimeUtil.dateToString(new Date(grant.getAddedAt()), "&7");
        }

        @Override
        public List<String> getDescription(Player player) {
            String addedBy = "Console";

            if (grant.getAddedBy() != null) {
                addedBy = "Could not fetch...";

                Profile addedByProfile = Profile.getByUuid(grant.getAddedBy());

                if (addedByProfile != null && addedByProfile.isLoaded()) {
                    addedBy = addedByProfile.getDisplayName();
                }
            }

            String removedBy = "Console";

            if (grant.getRemovedBy() != null) {
                removedBy = "Could not fetch...";

                Profile removedByProfile = Profile.getByUuid(grant.getRemovedBy());

                if (removedByProfile != null && removedByProfile.isLoaded()) {
                    removedBy = removedByProfile.getDisplayName();
                }
            }

            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
            lore.add(ChatColor.GREEN + "Rank: " + ChatColor.YELLOW + grant.getRank().getDisplayName());
            lore.add(ChatColor.GREEN + "Duration: " + ChatColor.YELLOW + grant.getDurationText());
            lore.add(ChatColor.GREEN + "Issued by: " + ChatColor.YELLOW + addedBy);
            lore.add(ChatColor.GREEN + "Reason: " + ChatColor.YELLOW + grant.getAddedReason());

            if (grant.isRemoved()) {
                lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
                lore.add(ChatColor.RED + "Grant Removed");
                lore.add(ChatColor.RED + TimeUtil.dateToString(new Date(grant.getRemovedAt()), "&7"));
                lore.add(ChatColor.RED + "Removed by: " + ChatColor.GRAY + removedBy);
                lore.add(ChatColor.RED + "Reason: \"" + grant.getRemovedReason() + "\"");
            } else {
                if (!grant.hasExpired()) {
                    lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");

                    if (player.hasPermission("engine.grants.remove")) {
                        lore.add(ChatColor.GREEN + "Right click to remove this grant");
                    } else {
                        lore.add(ChatColor.RED + "You cannot remove grants");
                    }
                }
            }

            lore.add(ChatColor.GOLD.toString() + ChatColor.STRIKETHROUGH + "------------------------");
            return lore;
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !grant.isRemoved() && !grant.hasExpired()) {
                if (player.hasPermission("engine.grant.remove")) {
                    GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.REMOVE, GrantProcedureStage.REQUIRE_TEXT);
                    procedure.setGrant(grant);
                    player.sendMessage(ColorText.translate("&6Type a reason for this procedure."));
                    player.closeInventory();
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "You don't have permissions for remove grants.");
                }
            }
        }
    }
}
