package com.cobelpvp.engine.grant;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.engine.util.TimeUtil;

import java.util.Date;
import java.util.UUID;

public class OGrant {

    public static GrantJsonSerializer SERIALIZER = new GrantJsonSerializer();
    public static GrantJsonDeserializer DESERIALIZER = new GrantJsonDeserializer();

    private final UUID uuid;
    private final Rank rank;
    private UUID addedBy;
    private final long addedAt;
    private final String addedReason;
    private final long duration;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;

    public OGrant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public boolean isPermanent() {
        return duration == Integer.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getExpiresAtDate() {
        if (duration == Integer.MAX_VALUE) {
            return "Never";
        }

        return TimeUtil.dateToString(new Date(addedAt + duration), "&7");
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

    @Override
    public boolean equals(Object object) {
        return object instanceof OGrant && ((OGrant) object).uuid.equals(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Rank getRank() {
        return rank;
    }

    public UUID getAddedBy() {
        return addedBy;
    }

    public long getAddedAt() {
        return addedAt;
    }

    public String getAddedReason() {
        return addedReason;
    }

    public long getDuration() {
        return duration;
    }

    public UUID getRemovedBy() {
        return removedBy;
    }

    public long getRemovedAt() {
        return removedAt;
    }

    public String getRemovedReason() {
        return removedReason;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setAddedBy(UUID addedBy) {
        this.addedBy = addedBy;
    }

    public void setRemovedBy(UUID removedBy) {
        this.removedBy = removedBy;
    }

    public void setRemovedAt(long removedAt) {
        this.removedAt = removedAt;
    }

    public void setRemovedReason(String removedReason) {
        this.removedReason = removedReason;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}