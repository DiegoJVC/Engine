package com.cobelpvp.engine.profile.menu.buttons;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class PunishmentsButton extends Button {

    private Profile profile;

    public PunishmentsButton(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.LIGHT_PURPLE + "Punishments";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GRAY.toString() + profile.getPunishments().size() + " punishments recorded.");

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.PAPER;
    }
}
