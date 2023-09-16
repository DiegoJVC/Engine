package com.cobelpvp.engine.punishment;

import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum PunishmentType {

    BLACKLIST("Blacklist", "blacklisted", "unblacklisted", true, true, new PunishmentTypeData("Blacklists", ChatColor.DARK_RED, 14)),
    BAN("Ban", "banned", "unbanned", true, true, new PunishmentTypeData("Bans", ChatColor.GOLD, 1)),
    MUTE("Mute", "muted", "unmuted", false, true, new PunishmentTypeData("Mutes", ChatColor.YELLOW, 4)),
    WARN("Warning", "warned", null, false, false, new PunishmentTypeData("Warnings", ChatColor.GREEN, 13)),
    KICK("Kick", "kicked", null, false, false, new PunishmentTypeData("Kicks", ChatColor.GRAY, 7)),
    BAN_IP("IP-Ban", "ip-banned", "ip-unbanned", true, true, new PunishmentTypeData("IP-Bans", ChatColor.DARK_RED, 14));

    private String readable;
    private String context;
    private String undoContext;
    private boolean ban;
    private boolean removable;
    private PunishmentTypeData typeData;

    PunishmentType(String readable, String context, String undoContext, boolean ban, boolean removable, PunishmentTypeData typeData) {
        this.readable = readable;
        this.context = context;
        this.undoContext = undoContext;
        this.ban = ban;
        this.removable = removable;
        this.typeData = typeData;
    }

    @Getter
    public static class PunishmentTypeData {

        private String readable;
        private ChatColor color;
        private int durability;

        public PunishmentTypeData(String readable, ChatColor color, int durability) {
            this.readable = readable;
            this.color = color;
            this.durability = durability;
        }

        public String getReadable() {
            return readable;
        }

        public ChatColor getColor() {
            return color;
        }

        public int getDurability() {
            return durability;
        }
    }

    public String getReadable() {
        return readable;
    }

    public String getContext() {
        return context;
    }

    public String getUndoContext() {
        return undoContext;
    }

    public boolean isBan() {
        return ban;
    }

    public boolean isRemovable() {
        return removable;
    }

    public PunishmentTypeData getTypeData() {
        return typeData;
    }
}
