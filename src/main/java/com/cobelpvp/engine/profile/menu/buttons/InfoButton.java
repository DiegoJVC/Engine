package com.cobelpvp.engine.profile.menu.buttons;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoButton extends Button {

    private Profile profile;

    public InfoButton(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GREEN + "Information";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();

        String lastSeen = new SimpleDateFormat("MM/dd/yyyy").format(new Date(profile.getLastSeen()));
        String firstSeen = new SimpleDateFormat("MM/dd/yyyy").format(new Date(profile.getFirstSeen()));

        if (profile.isLoaded()) {
            lore.add(ColorText.translate("&6Last Seen: &c" + lastSeen));
            lore.add(ColorText.translate("&6First Seen: &c" + firstSeen));
            lore.add(ColorText.translate("&6Country: &c" + profile.getCountry()));
            lore.add(ColorText.translate("&6Last Server: &c" + (profile.getLastServer() == null ? "&cNone" : profile.getLastServer())));
            lore.add(ColorText.translate("&6UUID: &c" + profile.getId()));
        } else {
            lore.add(ColorText.translate("&cProfile not currently loaded"));
        }

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BOOK;
    }
}
