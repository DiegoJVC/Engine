package com.cobelpvp.engine.profile.menu.menus;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.profile.menu.buttons.CloseButton;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.menu.Menu;
import com.cobelpvp.engine.profile.menu.buttons.InfoButton;
import com.cobelpvp.engine.profile.menu.buttons.PunishmentsButton;
import com.cobelpvp.engine.profile.menu.buttons.RankButton;
import org.bukkit.entity.Player;

import java.util.*;

public class PersonalProfileMenu extends Menu {

    private final Profile profile;

    public PersonalProfileMenu(Profile profile) {
        this.profile = profile;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 36;
    }

    @Override
    public String getTitle(Player player) {
        return "Profile";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(13, new InfoButton(profile));
        buttons.put(12, new RankButton(profile));
        buttons.put(14, new PunishmentsButton(profile));
        buttons.put(31, new CloseButton());

        return buttons;
    }
}
