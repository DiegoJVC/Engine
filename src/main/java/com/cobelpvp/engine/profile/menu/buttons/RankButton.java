package com.cobelpvp.engine.profile.menu.buttons;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.menu.Button;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankButton extends Button {

    private static List COLORS = new ArrayList(Arrays.asList(new ChatColor[]{ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE, ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.BLACK, ChatColor.DARK_GREEN, ChatColor.RED}));
    private Profile profile;

    public RankButton(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String getName(Player player) {
        return ChatColor.RESET + profile.getActiveGrant().getRank().getFormattedName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lore = new ArrayList<>();

        lore.add(ColorText.translate("&eThis is the current rank of " + profile.getDisplayName()));

        return lore;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.WOOL;
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte) convertChatColorToWoolData(ChatColor.getByChar(profile.getActiveGrant().getRank().getGameColor()
                .replace(String.valueOf('§'), "")
                .replace("&", "")
                .replace("o", "")));
    }

    public static int convertChatColorToWoolData(ChatColor color) {
        return color != ChatColor.DARK_RED && color != ChatColor.RED ? (color == ChatColor.DARK_GREEN ? 13 : (color == ChatColor.BLUE ? 11 : (color == ChatColor.DARK_PURPLE ? 10 : (color == ChatColor.DARK_AQUA ? 9 : (color == ChatColor.DARK_GRAY ? 7 : COLORS.indexOf(color)))))) : 14;
    }
}
