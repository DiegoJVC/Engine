package com.cobelpvp.engine.moderator.menu.buttons;

import com.cobelpvp.atheneum.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HealthButton extends Button {

    private Player target;

    public HealthButton(Player target) {
        this.target = target;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.GOLD + "Health";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                ChatColor.YELLOW.toString() + target.getHealth()
        );
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.APPLE;
    }

    @Override
    public int getAmount(Player player) {
        return (short) target.getHealth();
    }
}
