package com.cobelpvp.engine.profile.menu.buttons;

import com.cobelpvp.atheneum.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class CloseButton extends Button {

    @Override
    public String getName(Player player) {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Click to close";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.RED + "Click here to close menu");

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BED;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
    }
}
