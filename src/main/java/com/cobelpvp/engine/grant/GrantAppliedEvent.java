package com.cobelpvp.engine.grant;

import com.cobelpvp.engine.util.BaseEvent;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class GrantAppliedEvent extends BaseEvent {

    private Player player;
    private OGrant grant;

    public GrantAppliedEvent(Player player, OGrant grant) {
        this.player = player;
        this.grant = grant;
    }

    public Player getPlayer() {
        return player;
    }

    public OGrant getGrant() {
        return grant;
    }
}
