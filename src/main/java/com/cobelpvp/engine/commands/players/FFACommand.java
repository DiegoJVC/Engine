package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FFACommand {

    @Command(names = {"ffaeffects"}, permission = "engine.ffaeffects")
    public static void FFACommand(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
                players.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                players.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0));
                players.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 0));
                players.sendMessage(ChatColor.GOLD + "Now you have all the FFA effects.");
            }

        player.sendMessage(ChatColor.GREEN + "You have given all players potion effects.");
    }
}
