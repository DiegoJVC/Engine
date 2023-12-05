package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Flag;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HealCommand {

    private static final Set<PotionEffectType> NEGATIVE_EFFECTS = new HashSet<PotionEffectType>();

    @Command(names = {"heal"}, permission = "engine.heal", description = "Heal a player.")
    public static void heal(CommandSender sender, @Flag(value = {"p"}, description = "Clear all potion effects") boolean allPotions, @Param(name = "player", defaultValue = "self") Player target) {
        if (!sender.equals(target) && !sender.hasPermission("engine.heal.other")) {
            sender.sendMessage(ChatColor.RED + "No permission to heal other players.");
            return;
        }
        target.setFoodLevel(20);
        target.setSaturation(10.0f);
        target.setHealth(target.getMaxHealth());
        target.getActivePotionEffects().stream().filter(effect -> allPotions || NEGATIVE_EFFECTS.contains(effect.getType())).forEach(effect -> target.removePotionEffect(effect.getType()));
        target.setFireTicks(0);
        if (!sender.equals(target)) {
            sender.sendMessage(target.getDisplayName() + ChatColor.GOLD + " has been healed.");
        }
        target.sendMessage(ChatColor.GOLD + "You have been healed.");
    }

    static {
        Collections.addAll(NEGATIVE_EFFECTS, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.HARM, PotionEffectType.CONFUSION, PotionEffectType.BLINDNESS, PotionEffectType.HUNGER, PotionEffectType.WEAKNESS, PotionEffectType.POISON, PotionEffectType.WITHER);
    }
}

