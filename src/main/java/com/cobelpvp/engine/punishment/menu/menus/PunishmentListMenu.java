package com.cobelpvp.engine.punishment.menu.menus;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.menu.pagination.PaginatedMenu;
import com.cobelpvp.engine.punishment.menu.buttons.PunishmentInfoButton;
import org.bukkit.entity.Player;

import java.util.*;

public class PunishmentListMenu extends PaginatedMenu {

    private Profile profile;
    private PunishmentType punishmentType;

    public PunishmentListMenu(Profile profile, PunishmentType punishmentType) {
        this.profile = profile;
        this.punishmentType = punishmentType;
        lastMenu = new PunishmentCheckMenu(profile);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "" + punishmentType.getTypeData().getReadable() + " - " + profile.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Punishment punishment : profile.getPunishments()) {
            if (punishment.getType() == punishmentType) {
                buttons.put(buttons.size(), new PunishmentInfoButton(punishment, profile));
            }
        }

        return buttons;
    }


}
