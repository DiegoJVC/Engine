package com.cobelpvp.engine.commands.parameter;

import com.cobelpvp.atheneum.command.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnchantParameterType implements ParameterType<Enchantment> {

    private static final Map<String, Enchantment> MAP = new HashMap<>();

    static {
        MAP.put("sharp", Enchantment.DAMAGE_ALL);
        MAP.put("sharpness", Enchantment.DAMAGE_ALL);

        MAP.put("fire", Enchantment.FIRE_ASPECT);
        MAP.put("fireaspect", Enchantment.FIRE_ASPECT);

        MAP.put("knock", Enchantment.KNOCKBACK);
        MAP.put("knockback", Enchantment.KNOCKBACK);

        MAP.put("smite", Enchantment.DAMAGE_UNDEAD);

        MAP.put("baneof", Enchantment.DAMAGE_ARTHROPODS);
        MAP.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);

        MAP.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        MAP.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);

        MAP.put("fire", Enchantment.PROTECTION_FIRE);
        MAP.put("fireprot", Enchantment.PROTECTION_FIRE);
        MAP.put("fireprotection", Enchantment.PROTECTION_FIRE);

        MAP.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        MAP.put("projprotection", Enchantment.PROTECTION_PROJECTILE);

        MAP.put("looting", Enchantment.LOOT_BONUS_MOBS);

        MAP.put("falling", Enchantment.PROTECTION_FALL);
        MAP.put("featherfalling", Enchantment.PROTECTION_FALL);

        MAP.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);

        MAP.put("silk", Enchantment.SILK_TOUCH);
        MAP.put("silktouch", Enchantment.SILK_TOUCH);

        MAP.put("power", Enchantment.ARROW_DAMAGE);

        MAP.put("punch", Enchantment.ARROW_KNOCKBACK);

        MAP.put("flame", Enchantment.ARROW_FIRE);

        MAP.put("infinity", Enchantment.ARROW_INFINITE);

        MAP.put("durability", Enchantment.DURABILITY);
        MAP.put("unbreaking", Enchantment.DURABILITY);

        MAP.put("efficiency", Enchantment.DIG_SPEED);
    }

    @Override
    public Enchantment transform(CommandSender commandSender, String s) {
        if (!MAP.containsKey(s.toLowerCase())) {
            commandSender.sendMessage(ChatColor.RED + s + " doesn't exists.");
            return (null);
        }
        return MAP.get(s.toLowerCase());
    }

    @Override
    public List<String> tabComplete(Player player, Set<String> set, String s) {
        return (MAP.keySet().stream().filter(string -> StringUtils.startsWithIgnoreCase(string, s))).collect(Collectors.toList());
    }
}
