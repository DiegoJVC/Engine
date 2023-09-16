package com.cobelpvp.engine.punishment.menu.menus;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.menu.Menu;
import com.cobelpvp.engine.punishment.menu.buttons.SelectPunishmentTypeButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PunishmentCheckMenu extends Menu {

    private Profile profile;

    public PunishmentCheckMenu(Profile profile) {
        this.profile = profile;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    @Override
    public String getTitle(Player player) {
        return "Punishments of " + profile.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(9, new SelectPunishmentTypeButton(profile, PunishmentType.BLACKLIST));
        buttons.put(11, new SelectPunishmentTypeButton(profile, PunishmentType.BAN));
        buttons.put(13, new SelectPunishmentTypeButton(profile, PunishmentType.MUTE));
        buttons.put(15, new SelectPunishmentTypeButton(profile, PunishmentType.WARN));
        buttons.put(17, new SelectPunishmentTypeButton(profile, PunishmentType.KICK));

        return buttons;
    }
}