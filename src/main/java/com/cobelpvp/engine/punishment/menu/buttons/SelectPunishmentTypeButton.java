package com.cobelpvp.engine.punishment.menu.buttons;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.engine.punishment.menu.menus.PunishmentListMenu;
import com.cobelpvp.engine.util.ItemBuilder;
import com.cobelpvp.atheneum.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SelectPunishmentTypeButton extends Button {

    private Profile profile;
    private PunishmentType punishmentType;

    public SelectPunishmentTypeButton(Profile profile, PunishmentType punishmentType) {
        this.profile = profile;
        this.punishmentType = punishmentType;
    }

    @Override
    public String getName(Player player) {
        return null;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return null;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return getIcon();
    }


    public ItemStack getIcon() {
        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GRAY.toString() + profile.getPunishmentCountByType(punishmentType) + " " + (punishmentType.getTypeData().getReadable().toLowerCase()) + " on record");

        if (punishmentType == PunishmentType.BAN) {
            return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).durability((short) 1).build();
        }

        if (punishmentType == PunishmentType.MUTE) {
            return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).durability((short) 3).build();
        }

        if (punishmentType == PunishmentType.BLACKLIST) {
            return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).durability((short) 14).build();
        }

        if (punishmentType == PunishmentType.KICK) {
            return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).durability((short) 2).build();
        }

        if (punishmentType == PunishmentType.WARN) {
            return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).durability((short) 4).build();
        }

        return new ItemBuilder(Material.WOOL).name(punishmentType.getTypeData().getColor() + punishmentType.getTypeData().getReadable()).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();

        new PunishmentListMenu(profile, punishmentType).openMenu(player);
    }
}
