package com.cobelpvp.engine.moderator.menu;

import com.cobelpvp.engine.moderator.menu.buttons.PlayerNameButton;
import com.cobelpvp.engine.util.InventoryUtil;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.menu.Menu;
import com.cobelpvp.engine.moderator.menu.buttons.HealthButton;
import com.cobelpvp.engine.moderator.menu.buttons.PotionEffectButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class InvseeMenu extends Menu {

    private Player target;

    public InvseeMenu(Player target) {
        this.target = target;
    }

    @Override
    public String getTitle(Player player) {
        return "Inventory Inspector";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 6 * 9;
    }

    @Override
    public Map<Integer, Button> getButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();
        ItemStack[] items = InventoryUtil.fixInventoryOrder(target.getInventory().getContents());

        for (int i = 0; i < items.length; i++) {
            ItemStack stacks = items[i];
            if (stacks == null || stacks.getType() == Material.AIR) continue;
            buttons.put(i, Button.fromItem(stacks));
        }

        for (int i = 0; i < target.getInventory().getArmorContents().length; i++) {
            ItemStack stacks = target.getInventory().getArmorContents()[i];

            if (stacks == null || stacks.getType() == Material.AIR) continue;
            ;
            buttons.put(39 - i, Button.fromItem(stacks));
        }

        int position = 45;

        buttons.put(position++, new HealthButton(target));
        buttons.put(position++, new PotionEffectButton((List<PotionEffect>) target.getActivePotionEffects()));
        buttons.put(position++, new PlayerNameButton(target));

        return buttons;
    }
}
