package com.cobelpvp.engine.punishment;

import com.cobelpvp.engine.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.Bukkit;
import java.util.UUID;

@Getter
@Setter
public class Punishment {

    public static PunishmentJsonSerializer SERIALIZER = new PunishmentJsonSerializer();
    public static PunishmentJsonDeserializer DESERIALIZER = new PunishmentJsonDeserializer();

    private final UUID uuid;
    private final PunishmentType type;
    private UUID addedBy;
    final private long addedAt;
    private final String addedReason;
    final private long duration;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;


    public Punishment(UUID uuid, PunishmentType type, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.type = type;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public boolean isPermanent() {
        return type == PunishmentType.BLACKLIST || duration == Integer.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getDurationText() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        return TimeUtil.millisToRoundedTime(duration);
    }

    public String getTimeRemaining() {
        if (removed) {
            return "Removed";
        }

        if (isPermanent()) {
            return "Permanent";
        }

        if (hasExpired()) {
            return "Expired";
        }

        return TimeUtil.millisToRoundedTime((addedAt + duration) - System.currentTimeMillis());
    }

    public String getContext() {
        if (!(type == PunishmentType.BAN || type == PunishmentType.MUTE)) {
            return removed ? type.getUndoContext() : type.getContext();
        }

        if (isPermanent()) {
            return (removed ? type.getUndoContext() : " permanently " + type.getContext());
        } else {
            return (removed ? type.getUndoContext() : " temporarily " + type.getContext());
        }
    }

    public void broadcast(String sender, String target) {
        Bukkit.broadcastMessage(ColorText.translate("&r" + target + " &ahas been " + getContext() + " &aby &r" + sender + "&a."));
    }

    public String getKickMessage() {
        String kickMessage;
        if (type == PunishmentType.BAN) {
            kickMessage = "\n\n&cYour account is {context} from CobelPvP Network.{temporary}\n\n&cIf you feel this punishment is unjust, you may appeal at:\n\n&ehttps://discord.io/CobelPvP";
            String temporary = "";
            if (!isPermanent()) {
                temporary = "\n&cThis punishment expires in &e{time-remaining}&c.";
                temporary = temporary.replace("{time-remaining}", getTimeRemaining());
            }
            kickMessage = kickMessage.replace("{context}", getContext()).replace("{temporary}", temporary);
        } else if (type == PunishmentType.BLACKLIST) {
            kickMessage = "\n\n&cYour account is " + getContext() + " from CobelPvP Network.\n\n&cThis type of punishment cannot be appeal";
        } else if (type == PunishmentType.KICK) {
            kickMessage = "\n\n&cYou were kicked by a staff member.\nReason: &e" + addedReason + "";
        } else if (type == PunishmentType.BAN_IP) {
            kickMessage = "\n\n&cYour account is " + getContext() + " from CobelPvP Network.\n\n&cIf you feel this punishment is unjust, you may appeal at:\n\n&ehttps://discord.io/CobelPvP";
        } else {
            kickMessage = null;
        }

        return ColorText.translate(kickMessage);
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Punishment && ((Punishment) object).uuid.equals(uuid);
    }
}