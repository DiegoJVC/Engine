package com.cobelpvp.engine.holograms;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.hologram.FrozenHologramHandler;
import com.cobelpvp.atheneum.hologram.construct.Hologram;
import com.cobelpvp.engine.Engine;
import java.util.ArrayList;
import java.util.Map;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HologramCommands {

    @Command(names={"hologram create", "holo create"}, permission="engine.holograms")
    public static void hologram_create(Player sender, @Param(name="text", wildcard=true) String text) {
        Hologram hologram = FrozenHologramHandler.createHologram().at(sender.getEyeLocation()).addLines(text).build();
        hologram.send();
        int id = Engine.getInstance().getHologramManager().register(hologram);
        sender.sendMessage(ChatColor.GOLD + "Hologram " + ChatColor.WHITE + "#" + id + ChatColor.GOLD + " has been created.");
    }

    @Command(names={"hologram addline", "holo addline"}, permission="engine.holograms")
    public static void hologram_addline(Player sender, @Param(name="id") int id, @Param(name="text", wildcard=true) String text) {
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        hologram.addLines(text);
        Engine.getInstance().getHologramManager().save();
    }

    @Command(names={"hologram removeline", "holo removeline"}, permission="engine.holograms")
    public static void hologram_removeline(Player sender, @Param(name="id") int id, @Param(name="lineNumber") int line) {
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
            return;
        }
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        ArrayList<String> lines = new ArrayList<>(hologram.getLines());
        try {
            lines.remove(line);
            hologram.setLines(lines);
            Engine.getInstance().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success.");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
        }
    }

    @Command(names={"hologram insertbefore", "holo insertbefore"}, permission="engine.holograms")
    public static void hologram_insertbefore(Player sender, @Param(name="id") int id, @Param(name="beforeLineNumber") int line, @Param(name="text", wildcard=true) String text) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
            return;
        }
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        ArrayList<String> lines = new ArrayList<String>(hologram.getLines());
        try {
            lines.add(line, text);
            hologram.setLines(lines);
            Engine.getInstance().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success.");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
        }
    }

    @Command(names={"hologram insertafter", "holo insertafter"}, permission="engine.holograms")
    public static void hologram_insertafter(Player sender, @Param(name="id") int id, @Param(name="afterLineNumber") int line, @Param(name="text", wildcard=true) String text) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
            return;
        }
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        ArrayList<String> lines = new ArrayList<String>(hologram.getLines());
        try {
            lines.add(line + 1, text);
            hologram.setLines(lines);
            Engine.getInstance().getHologramManager().save();
            sender.sendMessage(ChatColor.GREEN + "Success.");
        }
        catch (IndexOutOfBoundsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
        }
    }

    @Command(names={"hologram edit", "holo edit"}, permission="engine.holograms")
    public static void hologram_edit(Player sender, @Param(name="id") int id, @Param(name="lineToEdit") int line, @Param(name="newText", wildcard=true) String newText) {
        if (--line < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid index.");
            return;
        }
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        hologram.setLine(line, newText);
        Engine.getInstance().getHologramManager().save();
        sender.sendMessage(ChatColor.GREEN + "Success.");
    }

    @Command(names={"hologram list", "holo list"}, permission="engine.holograms")
    public static void hologram_list(Player sender) {
        if (Engine.getInstance().getHologramManager().getHolograms().size() == 0) {
            sender.sendMessage(ChatColor.RED + "There are no active holograms.");
            return;
        }
        for (Map.Entry<Integer, Hologram> entry : Engine.getInstance().getHologramManager().getHolograms().entrySet()) {
            ArrayList<String> tooltip = new ArrayList<String>();
            tooltip.add(ChatColor.GREEN + "Location: " + String.format("[%.1f, %.1f, %.1f]", entry.getValue().getLocation().getX(), entry.getValue().getLocation().getY(), entry.getValue().getLocation().getZ()));
            tooltip.add(ChatColor.YELLOW + "Click to teleport");
            tooltip.add("");
            int i = 0;
            for (String line : entry.getValue().getLines()) {
                line = ChatColor.translateAlternateColorCodes('&', line);
                tooltip.add(ChatColor.GRAY.toString() + ++i + ". " + ChatColor.RESET + line);
            }
            FancyMessage message = new FancyMessage("#" + entry.getKey()).color(ChatColor.RED).tooltip(tooltip).command("/tppos " + String.format("%.1f %.1f %.1f", entry.getValue().getLocation().getX(), entry.getValue().getLocation().getY(), entry.getValue().getLocation().getZ()));
            message.send(sender);
        }
    }

    @Command(names={"hologram movehere", "holo tphere"}, permission="engine.holograms")
    public static void hologram_movehere(Player sender, @Param(name="id") int id) {
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        Engine.getInstance().getHologramManager().move(id, sender.getEyeLocation());
    }

    @Command(names={"hologram delete", "holo delete"}, permission="engine.holograms")
    public static void hologram_delete(Player sender, @Param(name="id") int id) {
        Hologram hologram = Engine.getInstance().getHologramManager().getHolograms().get(id);
        if (hologram == null) {
            sender.sendMessage(ChatColor.RED + "No hologram with that id found.");
            return;
        }
        Engine.getInstance().getHologramManager().getHolograms().remove(id);
        Engine.getInstance().getHologramManager().save();
        hologram.destroy();
        sender.sendMessage(ChatColor.YELLOW + "Deleted Hologram #" + id + "");
    }
}
