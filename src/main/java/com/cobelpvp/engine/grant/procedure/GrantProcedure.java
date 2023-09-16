package com.cobelpvp.engine.grant.procedure;

import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.rank.Rank;
import lombok.Getter;
import lombok.Setter;
import com.cobelpvp.engine.profile.Profile;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class GrantProcedure {

    @Getter
    private static final Set<GrantProcedure> procedures = new HashSet<>();

    @Getter
    private final Player issuer;
    @Getter
    private final Profile recipient;
    @Getter
    private final GrantProcedureType type;
    @Getter
    @Setter
    private Rank rank;
    @Getter
    private GrantProcedureStage stage;
    @Getter
    @Setter
    private OGrant grant;

    public GrantProcedure(Player issuer, Profile recipient, GrantProcedureType type, GrantProcedureStage stage) {
        this.issuer = issuer;
        this.recipient = recipient;
        this.type = type;
        this.stage = stage;

        procedures.add(this);
    }

    public void finish() {
        recipient.save();
        recipient.checkGrants();

        procedures.remove(this);
    }

    public void cancel() {
        procedures.remove(this);
    }

    public static GrantProcedure getByPlayer(Player player) {
        for (GrantProcedure procedure : procedures) {
            if (procedure.issuer.equals(player)) {
                return procedure;
            }
        }

        return null;
    }

    public static Set<GrantProcedure> getProcedures() {
        return procedures;
    }

    public Player getIssuer() {
        return issuer;
    }

    public Profile getRecipient() {
        return recipient;
    }

    public GrantProcedureType getType() {
        return type;
    }

    public Rank getRank() {
        return rank;
    }

    public GrantProcedureStage getStage() {
        return stage;
    }

    public OGrant getGrant() {
        return grant;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setStage(GrantProcedureStage stage) {
        this.stage = stage;
    }

    public void setGrant(OGrant grant) {
        this.grant = grant;
    }
}