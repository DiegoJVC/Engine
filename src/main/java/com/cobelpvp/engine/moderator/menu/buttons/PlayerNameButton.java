package com.cobelpvp.engine.moderator.menu.buttons;

import com.cobelpvp.atheneum.menu.Button;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PlayerNameButton extends Button {

    private Player target;

    public PlayerNameButton(Player target) {
        this.target = target;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Name";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                ChatColor.GREEN + target.getName()
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.NAME_TAG;
    }
}
